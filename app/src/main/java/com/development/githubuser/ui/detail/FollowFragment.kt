package com.development.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.development.githubuser.R
import com.development.githubuser.databinding.FragmentFollowBinding
import com.development.githubuser.data.remote.response.ItemsItem
import com.development.githubuser.ui.main.MainAdapter
import com.development.githubuser.utils.MessageCallback

class FollowFragment : Fragment() {

    private var binding: FragmentFollowBinding? = null
    private lateinit var tvNoUser: TextView
    private lateinit var imgNoUser: ImageView
    private val viewModel by activityViewModels<DetailViewModel>()
    private var type = 0
    private val adapter by lazy {
        MainAdapter { user ->
            startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("item", user)
            })
        }
    }

    private fun manageMessageFollows(message: MessageCallback) {
        when (message) {
            is MessageCallback.Success<*> -> {
                val userList = message.data as MutableList<ItemsItem>
                if (userList.isEmpty()) {
                    tvNoUser.visibility = View.VISIBLE
                    imgNoUser.visibility = View.VISIBLE
                    binding?.rvFollows?.visibility = View.GONE
                } else {
                    tvNoUser.visibility = View.GONE
                    imgNoUser.visibility = View.GONE
                    binding?.rvFollows?.visibility = View.VISIBLE
                    adapter.setData(userList)
                }
            }

            is MessageCallback.Error -> {
                Toast.makeText(
                    requireContext(), message.exception.message.toString(), Toast.LENGTH_SHORT
                ).show()
            }

            is MessageCallback.Loading -> {
                binding?.progressBar?.isVisible = message.isLoading
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFollowBinding.bind(view)

        binding?.let { b ->
            tvNoUser = b.root.findViewById(R.id.tvNoUsersDetailFound)
            imgNoUser = b.root.findViewById(R.id.imgNoUsersDetailFound)
            binding?.rvFollows?.layoutManager = LinearLayoutManager(requireContext())
            binding?.rvFollows?.setHasFixedSize(true)
            binding?.rvFollows?.adapter = adapter
        }

        viewModel.resultFollowersUser.observe(viewLifecycleOwner, this::manageMessageFollows)
        viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageMessageFollows)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val FOLLOWERS = 100
        const val FOLLOWING = 200
        fun newInstance(type: Int) = FollowFragment().apply {
            this.type = type
        }
    }
}

