package com.example.recyclersample.favePage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclersample.data.PersonFave
import com.example.recyclersample.data.PersonFaveSource
import com.example.recyclersample.databinding.ActivityFaveBinding
import com.example.recyclersample.detailPage.PersonDetailActivity
import com.example.recyclersample.mainPage.PersonListViewModelFactory
import com.example.recyclersample.prefer.PreferenceSetting

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PersonFaveActivity : AppCompatActivity() {

    private lateinit var faveBinding: ActivityFaveBinding
    private lateinit var faveRecyclerView: RecyclerView
    private lateinit var faveViewModel: PersonFaveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faveBinding = ActivityFaveBinding.inflate(layoutInflater)
        setContentView(faveBinding.root)

        val pref = PreferenceSetting.getInstance(dataStore)

        val viewModelFactory = PersonListViewModelFactory(this@PersonFaveActivity.application, "", pref)
        faveViewModel = ViewModelProvider(this@PersonFaveActivity, viewModelFactory)[PersonFaveViewModel::class.java]

        faveViewModel.faveList.observe(this) { favoriteUserList ->
            if(favoriteUserList.isEmpty()) {
                faveBinding.personFaveRV.visibility = View.GONE
                faveBinding.noFave.visibility = View.VISIBLE
            } else {
                faveBinding.personFaveRV.visibility = View.VISIBLE
                faveBinding.noFave.visibility = View.GONE
                showFavoriteUserList(favoriteUserList)
            }
        }

        faveRecyclerView = faveBinding.personFaveRV
        faveRecyclerView.setHasFixedSize(true)
    }

    private fun showFavoriteUserList(favoriteUserList: List<PersonFave>) {
        faveRecyclerView.layoutManager = LinearLayoutManager(this)
        val faveAdapter = PersonFaveAdapter(favoriteUserList, PersonFaveSource(this@PersonFaveActivity.application))
        faveRecyclerView.adapter = faveAdapter

        faveAdapter.setOnItemClickCallback(object : PersonFaveAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PersonFave) {
                val intentToDetail = Intent(this@PersonFaveActivity, PersonDetailActivity::class.java)
                intentToDetail.putExtra(EXTRA_FAVORITE, data)
                startActivity(intentToDetail)
            }
        })
    }

    companion object {
        const val EXTRA_FAVORITE = "extra-favorite"
    }
}