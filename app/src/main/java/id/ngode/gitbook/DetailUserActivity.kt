package id.ngode.gitbook

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.ngode.gitbook.adapter.DetailUserPagerAdapter
import id.ngode.gitbook.databinding.ActivityDetailUserBinding
import id.ngode.gitbook.model.DetailUserViewModel
import id.ngode.gitbook.model.UserResponse
import kotlin.math.ln
import kotlin.math.pow

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var user: UserResponse
    private var userFavoriteResponse: UserResponse = UserResponse()
    private var avatarUrl: String? = null
    private var profileUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            Glide.with(root)
                .load(R.drawable.ic_bg_detail)
                .apply(RequestOptions.centerCropTransform())
                .into(ivPhotoBg)
        }

        user = intent.getParcelableExtra<UserResponse>(EXTRA_USER) as UserResponse
        detailUserViewModel = ViewModelProvider(this, DetailUserViewModel.Factory(user.login, application)).get(DetailUserViewModel::class.java)

        val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.7f) //the alpha of the underlying children
            .setHighlightAlpha(0.6f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        detailUserViewModel.detailUser.observe(this, { detailUser ->
            binding.apply {
                tvFullName.text = detailUser?.name
                tvUsername.text = StringBuilder("@").append(detailUser?.login)
                tvCompany.text = detailUser?.company
                tvLocation.text = detailUser?.location
                tvRepository.text = getFormattedNumber(detailUser?.publicRepos?.toLong())
                tvFollowers.text = getFormattedNumber(detailUser?.followers?.toLong())
                tvFollowing.text = getFormattedNumber(detailUser?.following?.toLong())
                avatarUrl = detailUser?.avatarUrl
                profileUrl = detailUser?.htmlUrl

                Glide.with(root)
                    .load(detailUser?.avatarUrl)
                    .circleCrop()
                    .placeholder(shimmerDrawable)
                    .error(shimmerDrawable)
                    .into(ivPhoto)

                btnShare.setOnClickListener {
                    ShareCompat.IntentBuilder(root.context)
                        .setType("text/plain")
                        .setChooserTitle(getString(R.string.share_profile_url))
                        .setText(getString(R.string.share_url_message, detailUser?.name, detailUser?.login))
                        .startChooser()
                }
            }
        })

        detailUserViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        detailUserViewModel.toastText.observe(this, {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
            }
        })

        detailUserViewModel.countUser(user.login.toString()).observe(this, {
            if (it > 0) {
                binding.apply {
                    fabFavorite.visibility = View.GONE
                    fabDelete.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    fabFavorite.visibility = View.VISIBLE
                    fabDelete.visibility = View.GONE
                }
            }
        })

        val detailUserPagerAdapter = DetailUserPagerAdapter(this, user.login)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = detailUserPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.fabFavorite.setOnClickListener { view ->
            if (view.id == R.id.fab_favorite) {
                userFavoriteResponse.apply {
                    id = user.id
                    login = user.login
                    avatarUrl = user.avatarUrl
                    htmlUrl = user.htmlUrl
                }

                detailUserViewModel.addFavoriteUser(userFavoriteResponse)
                Toast.makeText(this, getString(R.string.favorite_added), Toast.LENGTH_SHORT).show()
            }
        }

        binding.fabDelete.setOnClickListener { view ->
            if (view.id == R.id.fab_delete) {
                userFavoriteResponse.apply {
                    id = user.id
                    login = user.login
                    avatarUrl = user.avatarUrl
                    htmlUrl = user.htmlUrl
                }

                detailUserViewModel.deleteFavoriteUser(userFavoriteResponse)
                Toast.makeText(this, getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener { view ->
            if (view.id == R.id.btn_back) {
                finish()
            }
        }
    }

    private fun getFormattedNumber(count: Long?): String {
        if (count != null) {
            if (count < 1000) return "" + count
            val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
            return String.format("%.1f %c", count / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
        }
        return "0"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_following,
            R.string.tab_followers
        )
    }
}