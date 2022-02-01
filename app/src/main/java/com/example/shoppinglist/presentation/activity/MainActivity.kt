package com.example.shoppinglist.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.presentation.viewmodel.MainViewModel
import com.example.shoppinglist.presentation.fragment.ShopItemFragment
import com.example.shoppinglist.presentation.adapter.ShopListAdapter

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.liveData.observe(this) {
            shopListAdapter.submitList(it)
        }
        binding.buttonAddShopItem.setOnClickListener {
            if (!isLandscapeOrientation()) {
                val intent = ShopItemActivity.newIntentAddMode(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    private fun launchFragment(fragment: Fragment) {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.shop_item_fragment_container_in_land, fragment)
                .addToBackStack(null)
                .commit()
        }

    private fun setupRecyclerView() {
        with(binding.rvShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupItemTouchHelper(binding.rvShopList)
    }

    private fun setupItemTouchHelper(rvShopList: RecyclerView?) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int,
            ) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        })
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if (!isLandscapeOrientation()) {
                val intent = ShopItemActivity.newIntentEditMode(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }


    private fun isLandscapeOrientation() = binding.shopItemFragmentContainerInLand != null

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success !", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }
}