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
import com.example.recyclersample.api.*
import com.example.recyclersample.data.PersonFave
import com.example.recyclersample.databinding.FragmentFollowingBinding
import com.example.recyclersample.detailPage.PersonDetailViewModel
import com.example.recyclersample.favePage.PersonFaveActivity
import com.example.recyclersample.mainPage.PersonListAdapter
import com.example.recyclersample.mainPage.PersonListActivity
import com.example.recyclersample.mainPage.PersonListViewModelFactory
import com.example.recyclersample.prefer.PreferenceSetting

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FollowingFragment : Fragment() {

    private lateinit var fragmentFollowingBinding: FragmentFollowingBinding
    private lateinit var detailViewModel: PersonDetailViewModel





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowingBinding = FragmentFollowingBinding.inflate(inflater,container,false)

        val pref = PreferenceSetting.getInstance(requireContext().dataStore)
        val viewModelFactory = PersonListViewModelFactory(this@FollowingFragment
            .requireActivity().application, "", pref)
        detailViewModel = ViewModelProvider(this@FollowingFragment
            .requireActivity(), viewModelFactory)[PersonDetailViewModel::class.java]

        val userDetail : UserDetail? = requireActivity().intent.getParcelableExtra(
            PersonListActivity.EXTRA_DATA)
        val faveUser : PersonFave? = requireActivity().intent.getParcelableExtra(
            PersonFaveActivity.EXTRA_FAVORITE)
        val username : String = userDetail?.login ?: faveUser?.login.toString()
        fragmentFollowingBinding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        detailViewModel.displayFollowingList(username)

        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        detailViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }

        detailViewModel.followingList.observe(viewLifecycleOwner) {
            val adapter = PersonListAdapter(it)
            fragmentFollowingBinding.apply {
                rvFollowing.adapter = adapter
            }
        }

        detailViewModel.isError.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Data not found!", Toast.LENGTH_SHORT).show()
            detailViewModel.doneToastErrorInput()
        }
        return fragmentFollowingBinding.root
    }





    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        fragmentFollowingBinding.rvFollowing.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        fragmentFollowingBinding.rvFollowing.addItemDecoration(itemDecoration)

    }





    private fun showLoading(isLoading: Boolean) = fragmentFollowingBinding.barProgress.isVisible == isLoading
}