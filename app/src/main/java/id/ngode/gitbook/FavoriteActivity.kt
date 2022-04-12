package id.ngode.gitbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.ngode.gitbook.adapter.ListUserAdapter
import id.ngode.gitbook.databinding.ActivityFavoriteBinding
import id.ngode.gitbook.model.FavoriteViewModel
import id.ngode.gitbook.model.UserResponse

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, FavoriteViewModel.Factory(application)).get(FavoriteViewModel::class.java)

        viewModel.getFavoriteUser().observe(this, { listFavoriteUser ->
            if (listFavoriteUser.isNotEmpty()) {
                binding.apply {
                    emptyImage.visibility = View.GONE
                    emptyText.visibility = View.GONE
                }
                setUsersListData(listFavoriteUser)
            } else {
                binding.apply {
                    emptyImage.visibility = View.VISIBLE
                    emptyText.visibility = View.VISIBLE
                }
                setUsersListData(listFavoriteUser)
            }
        })

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        binding.rvFavorite.setHasFixedSize(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUsersListData(usersList: List<UserResponse>) {
        val listUsers = ArrayList<UserResponse>()
        for (users in usersList) {
            listUsers.add(users)
        }
        val adapter = ListUserAdapter(listUsers)
        binding.rvFavorite.adapter = adapter

        adapter.setOnUserClickCallback(object : ListUserAdapter.OnUserClickCallback {
            override fun onUserClicked(data: UserResponse) {
                val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            favoriteShimmer.visibility = if (isLoading) View.VISIBLE else View.GONE
            rvFavorite.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }
}