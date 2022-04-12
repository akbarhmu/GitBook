package id.ngode.gitbook.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.ngode.gitbook.adapter.ListFollowingAdapter
import id.ngode.gitbook.databinding.FragmentFollowingBinding
import id.ngode.gitbook.model.DetailUserViewModel
import id.ngode.gitbook.model.UserResponse

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollowing.layoutManager = layoutManager
        binding.rvFollowing.setHasFixedSize(true)

        val detailUserViewModel = ViewModelProvider(this, DetailUserViewModel.Factory(arguments?.getString(EXTRA_USERNAME), requireActivity().application)).get(DetailUserViewModel::class.java)
        detailUserViewModel.getUserFollowing()
        detailUserViewModel.listUserFollowing.observe(viewLifecycleOwner, { listUserFollowing ->
            setUsersListData(listUserFollowing)
        })
        detailUserViewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.shimmerFollowing.visibility = View.VISIBLE
                binding.rvFollowing.visibility = View.GONE
            } else {
                binding.shimmerFollowing.visibility = View.GONE
                binding.rvFollowing.visibility = View.VISIBLE
            }
        })
    }

    private fun setUsersListData(usersList: List<UserResponse>) {
        val listUsers = ArrayList<UserResponse>()
        for (users in usersList) {
            listUsers.add(users)
        }
        val adapter = ListFollowingAdapter(listUsers)
        binding.rvFollowing.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var EXTRA_USERNAME = "EXTRA_USERNAME"
    }
}