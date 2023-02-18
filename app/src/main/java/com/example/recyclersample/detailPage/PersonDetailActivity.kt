package com.example.recyclersample.detailPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.recyclersample.R
import com.example.recyclersample.api.UserDetail
import com.example.recyclersample.data.PersonFave
import com.example.recyclersample.databinding.ActivityDetailBinding
import com.example.recyclersample.favePage.PersonFaveActivity
import com.example.recyclersample.fragment.SectionPagerAdapter
import com.example.recyclersample.mainPage.PersonListActivity
import com.example.recyclersample.mainPage.PersonListActivity.Companion.TAB_TITLES
import com.example.recyclersample.mainPage.PersonListViewModelFactory
import com.example.recyclersample.prefer.PreferenceSetting
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PersonDetailActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var detailViewModel:PersonDetailViewModel
    companion object {
        const val EXTRA_PERSON = "extra_person"
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)


        val pref = PreferenceSetting.getInstance(dataStore)


        val userDetail= intent.getParcelableExtra<UserDetail?>(PersonListActivity.EXTRA_DATA)
        val faveUser = intent.getParcelableExtra<PersonFave?>(PersonFaveActivity.EXTRA_FAVORITE)
        val username : String = userDetail?.login ?: faveUser?.login.toString()

        showLoading(true)
        val viewModelFactory = PersonListViewModelFactory(this@PersonDetailActivity.application,
            username, pref)
        detailViewModel = ViewModelProvider(this@PersonDetailActivity,
            viewModelFactory)[PersonDetailViewModel::class.java]
        userDetail.let{
            if(it!=null){
                detailViewModel.displayUserDetail(username)
            }
        }



        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        detailViewModel.detailUser.observe(this){
            showLoading(false)
            detailBinding.personDetailUsername.text = it.login
            detailBinding.personDetailName.text = it.name
            detailBinding.personDetailLocation.text = it.location
            detailBinding.personDetailCompany.text = it.company
            detailBinding.personDetailRepository.text = it.publicRepos
            detailBinding.personDetailFollowers.text = it.followers
            detailBinding.personDetailFollowing.text = it.following
            Glide.with(this)
                .load(it.avatarUrl)
                .circleCrop()
                .into(detailBinding.personDetailAvatar)
        }
        detailViewModel.isError.observe(this) {
            Toast.makeText(this, "Data not found!", Toast.LENGTH_SHORT).show()
            detailViewModel.doneToastErrorInput()
        }
        detailViewModel.faveExist.observe(this) { faveExist ->
            if (faveExist) {
                detailBinding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(
                    baseContext, R.drawable.ic_baseline_favorite_24))
            } else {
                detailBinding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(
                    baseContext, R.drawable.ic_baseline_favorite_border_24))
            }
        }
        detailBinding.fabFavorite.setOnClickListener {
            val favored = faveUser?: PersonFave(
                    id = userDetail!!.id,
                    avatarUrl = userDetail.avatarUrl,
                    login = userDetail.login,
                    type = userDetail.type
                )
            if (detailViewModel.checkIfFaveExist()!!) {
                detailViewModel.deleteFave(favored)

            } else {
                detailViewModel.addFave(favored)
            }
        }

        /**share button.*/
        val personSharingButton: Button = findViewById(R.id.action_share)
        personSharingButton.setOnClickListener(this)
    }





    /**sharing is caring.*/
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.action_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                startActivity(shareIntent)
            }
        }
    }





    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            detailBinding.barProgressDetail.visibility = View.VISIBLE
        } else {
            detailBinding.barProgressDetail.visibility = View.GONE
        }
    }
}


