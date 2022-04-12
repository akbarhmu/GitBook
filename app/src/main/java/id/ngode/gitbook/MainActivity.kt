package id.ngode.gitbook

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.ngode.gitbook.adapter.ListUserAdapter
import id.ngode.gitbook.databinding.ActivityMainBinding
import id.ngode.gitbook.model.MainViewModel
import id.ngode.gitbook.model.UserResponse

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var keyword: String
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        viewModel.listSearchUser.observe(this, { listSearchUser ->
            setUsersListData(listSearchUser)
        })

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

        viewModel.toastText.observe(this, {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)
        binding.rvUser.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                keyword = query.trim()
                viewModel.getSearchUser(keyword)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    keyword = newText.trim()
                    viewModel.getSearchUser(keyword)
                    return true
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val mIntent = Intent(this, SettingActivity::class.java)
                startActivity(mIntent)
                true
            }
            R.id.action_favorite -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
                true
            }
            else -> true
        }
    }

    private fun setUsersListData(usersList: List<UserResponse?>?) {
        val listUsers = ArrayList<UserResponse>()
        if (usersList != null) {
            for (users in usersList) {
                if (users != null) {
                    listUsers.add(users)
                }
            }
        }
        val adapter = ListUserAdapter(listUsers)
        binding.rvUser.adapter = adapter

        adapter.setOnUserClickCallback(object : ListUserAdapter.OnUserClickCallback {
            override fun onUserClicked(data: UserResponse) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            shimmerLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
            rvUser.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }
}