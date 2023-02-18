package com.example.recyclersample.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclersample.api.UserDetail
import com.example.recyclersample.data.PersonFave
import com.example.recyclersample.databinding.FragmentFollowersBinding
import com.example.recyclersample.detailPage.PersonDetailViewModel
import com.example.recyclersample.favePage.PersonFaveActivity
import com.example.recyclersample.mainPage.PersonListAdapter
import com.example.recyclersample.mainPage.PersonListActivity
import com.example.recyclersample.mainPage.PersonListViewModelFactory
import com.example.recyclersample.prefer.PreferenceSetting

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FollowersFragment : Fragment() {

    private lateinit var fragmentFollowersBinding: FragmentFollowersBinding
    private lateinit var detailViewModel: PersonDetailViewModel





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowersBinding = FragmentFollowersBinding.inflate(inflater,container,false)

        val pref = PreferenceSetting.getInstance(requireContext().dataStore)
        val viewModelFactory = PersonListViewModelFactory(this@FollowersFragment
            .requireActivity().application, "", pref)
        detailViewModel = ViewModelProvider(this@FollowersFragment
            .requireActivity(), viewModelFactory)[PersonDetailViewModel::class.java]

        val userDetail : UserDetail? = requireActivity().intent.getParcelableExtra(
            PersonListActivity.EXTRA_DATA)
        val faveUser : PersonFave? = requireActivity().intent.getParcelableExtra(
            PersonFaveActivity.EXTRA_FAVORITE)
        val username : String = userDetail?.login ?: faveUser?.login.toString()
        fragmentFollowersBinding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        detailViewModel.displayFollowersList(username)

        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        detailViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }

        detailViewModel.followersList.observe(viewLifecycleOwner) {
            val adapter = PersonListAdapter(it)
            fragmentFollowersBinding.apply {
                rvFollowers.adapter = adapter
            }
        }

        detailViewModel.isError.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Data not found!", Toast.LENGTH_SHORT).show()
            detailViewModel.doneToastErrorInput()
        }

        return fragmentFollowersBinding.root
    }





    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        fragmentFollowersBinding.rvFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        fragmentFollowersBinding.rvFollowers.addItemDecoration(itemDecoration)
    }





    private fun showLoading(isLoading: Boolean) = fragmentFollowersBinding.barProgress.isVisible == isLoading
}