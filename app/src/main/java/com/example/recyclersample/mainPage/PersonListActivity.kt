package com.example.recyclersample.mainPage


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclersample.detailPage.PersonDetailActivity
import com.example.recyclersample.R
import com.example.recyclersample.api.UserDetail
import com.example.recyclersample.databinding.ActivityPersonListBinding
import com.example.recyclersample.detailPage.AboutActivity
import com.example.recyclersample.favePage.PersonFaveActivity
import com.example.recyclersample.prefer.PreferenceSetting
import retrofit2.Call
import retrofit2.Response


class PersonListActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var listBinding: ActivityPersonListBinding
    private lateinit var listViewModel: PersonListViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var listRecyclerView: RecyclerView
    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
        const val EXTRA_DATA = "extra_data"
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listBinding=ActivityPersonListBinding.inflate(layoutInflater)
        setContentView(listBinding.root)

        val pref=PreferenceSetting.getInstance(dataStore)

        searchAdapter = SearchAdapter()
        searchAdapter.setOnItemClickCallback(object : SearchAdapter.OnItemClickCallback{
            override fun onItemCLicked(data: UserDetail) {
                Intent(this@PersonListActivity, PersonDetailActivity::class.java)
                    .also {
                        it.putExtra(PersonDetailActivity.EXTRA_PERSON, data.login)
                        startActivity(it)
                    }
            }
        })

        val searchViewModelFactory=PersonListViewModelFactory(this@PersonListActivity.application,"",pref)
        listViewModel = ViewModelProvider(this, searchViewModelFactory)[PersonListViewModel::class.java]

        listBinding.apply {
            rvPersonList.layoutManager = LinearLayoutManager(this@PersonListActivity)
            rvPersonList.setHasFixedSize(true)
            rvPersonList.adapter = searchAdapter
            iconSearch.setOnClickListener {
                searchPerson()
            }
            editInputLayout.setOnKeyListener { _, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    searchPerson()
                    showLoading(false)
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        listViewModel.personList.observe(this) { personList -> showRecyclerList(personList) }
        listViewModel.isLoading.observe(this) { showLoading(it) }
        listViewModel.isError.observe(this) {
            Toast.makeText(this, "No data!", Toast.LENGTH_SHORT).show()
            listViewModel.doneToastErrorInput()
        }
        listViewModel.isDarkMode.observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        listViewModel.getUserList().observe(this) {
            if (it != null) {
                searchAdapter.setList(it)
                showLoading(false)
            }
        }
        listRecyclerView = listBinding.rvPersonList
        listRecyclerView.setHasFixedSize(true)
    }





    private fun showRecyclerList(personList : List<UserDetail>) {
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        val personListAdapter = PersonListAdapter(personList)
        listRecyclerView.adapter = personListAdapter

        personListAdapter.setOnItemClickCallback(object: PersonListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserDetail) {
                val intent = Intent(this@PersonListActivity,
                    PersonDetailActivity::class.java)
                intent.putExtra(EXTRA_DATA,data)
                startActivity(intent)
            }
        })
    }







    private fun searchPerson() {
        listBinding.apply {
            val query = editInputLayout.text.toString()
            if (query.isNotEmpty()) {
                showLoading(true)
                listViewModel.setUserList(query)
            }
        }
    }









    /**making the menu*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }








    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_search -> {
                val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
                val searchItem : MenuItem = item
                val searchView = searchItem.actionView as SearchView

                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = resources.getString(R.string.search_hint)

                searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                        return true
                    }
                    override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                        listViewModel.displayUserList()
                        return true
                    }
                })

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        listViewModel.setUserList(query.toString())
                        searchView.clearFocus()
                        return true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
                return true
            }
            R.id.action_lighting -> {
                val isDarkMode = listViewModel.checkDarkMode()!!
                listViewModel.saveThemeSetting(!isDarkMode)
                invalidateOptionsMenu()
                return true
            }
            R.id.action_favorite -> {
                val intentToFavorite = Intent(this@PersonListActivity, PersonFaveActivity::class.java)
                startActivity(intentToFavorite)
                return true
            }
            R.id.action_about -> {
                val moveIntent = Intent(this@PersonListActivity, AboutActivity::class.java)
                startActivity(moveIntent)
                return true
            }
            else -> return true
        }
    }





    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isDarkMode: Boolean = listViewModel.checkDarkMode() ?: return true
        val modeMenu = menu?.findItem(R.id.action_lighting)
        if (isDarkMode) {
            modeMenu?.setIcon(R.drawable.ic_light_mode)
        } else {
            modeMenu?.setIcon(R.drawable.ic_dark_mode)
        }
        return super.onPrepareOptionsMenu(menu)
    }





    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            listBinding.barProgress.visibility = View.VISIBLE
        } else {
            listBinding.barProgress.visibility = View.GONE
        }
    }
}
