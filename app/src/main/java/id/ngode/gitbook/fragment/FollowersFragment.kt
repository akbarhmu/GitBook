package id.ngode.gitbook.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.ngode.gitbook.adapter.ListFollowersAdapter
import id.ngode.gitbook.databinding.FragmentFollowersBinding
import id.ngode.gitbook.model.DetailUserViewModel
import id.ngode.gitbook.model.UserResponse

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollowers.layoutManager = layoutManager
        binding.rvFollowers.setHasFixedSize(true)

        val detailUserViewModel = ViewModelProvider(this, DetailUserViewModel.Factory(arguments?.getString(FollowingFragment.EXTRA_USERNAME), requireActivity().application)).get(DetailUserViewModel::class.java)
        detailUserViewModel.getUserFollowers()
        detailUserViewModel.listUserFollowers.observe(viewLifecycleOwner, { listUserFollowers ->
            setUsersListData(listUserFollowers)
        })
        detailUserViewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.shimmerFollowers.visibility = View.VISIBLE
                binding.rvFollowers.visibility = View.GONE
            } else {
                binding.shimmerFollowers.visibility = View.GONE
                binding.rvFollowers.visibility = View.VISIBLE
            }
        })
    }

    private fun setUsersListData(usersList: List<UserResponse>) {
        val listUsers = ArrayList<UserResponse>()
        for (users in usersList) {
            listUsers.add(users)
        }
        val adapter = ListFollowersAdapter(listUsers)
        binding.rvFollowers.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var EXTRA_USERNAME = "EXTRA_USERNAME"
    }
}