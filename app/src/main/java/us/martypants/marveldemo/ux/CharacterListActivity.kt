package us.martypants.marveldemo.ux

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import us.martypants.marvel.App
import us.martypants.marvel.errorDialog
import us.martypants.marvel.models.Result
import us.martypants.marvel.view.BindingAdapter
import us.martypants.marvel.view.BindingViewHolder
import us.martypants.marvel.view.EndlessRecyclerViewScrollListener
import us.martypants.marveldemo.R
import us.martypants.marveldemo.databinding.ActivityMainBinding
import us.martypants.marveldemo.databinding.ItemBinding
import us.martypants.marveldemo.util.MyViewModelFactory

class CharacterListActivity : AppCompatActivity() {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private var adapter: RecyclerView.Adapter<BindingViewHolder<ItemBinding>>? = null

    val layoutMgr = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        App.app?.userComponent?.inject(this)
        viewModel =
            ViewModelProviders.of(this, MyViewModelFactory(application as App))
                .get(CharacterViewModel::class.java)
        mainBinding.recycler.setHasFixedSize(true)
        mainBinding.recycler.layoutManager = layoutMgr
        mainBinding.recycler.addOnScrollListener(scrollListener)

        viewModel.getCharacterList(viewModel.currentPage)
        setupObservers()
    }

    private fun setupObservers() {
        // Data
        viewModel.characterList.observe(this, {
            it?.let {
                if (it.isNotEmpty()) {
                    if (adapter == null) {
                        setupRecycler(it)
                    } else {
                        adapter?.notifyDataSetChanged()
                    }
                } else {
                    errorDialog(this, getString(R.string.no_results))
                    mainBinding.recycler.visibility = View.GONE
                }
            }
        })

        // Events
        lifecycleScope.launchWhenStarted {
            for (event in viewModel.eventChannel) {
                when (event) {
                    is CharacterViewModel.Event.DismissView -> finish()
                    is CharacterViewModel.Event.ShowErrorDialog -> showErrorMsg(event.error.localizedMessage ?: "Error")
                    is CharacterViewModel.Event.ShowDetailView -> showDetailActivity(event.characterId)
                }
            }

        }
    }

    private fun showDetailActivity(id: Int) {
        val intent = Intent(this@CharacterListActivity, CharacterDetailActivity::class.java)
        intent.putExtra(CharacterDetailActivity.DETAIL, id)
        startActivity(intent)
    }

    private fun showErrorMsg(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private val scrollListener = object : EndlessRecyclerViewScrollListener(layoutMgr) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
            viewModel.getNextPageOffset()
        }

    }

    private fun setupRecycler(list: List<Result>) {
        mainBinding.recycler.visibility = View.VISIBLE

        adapter = object : BindingAdapter<ItemBinding>(R.layout.item) {
            override fun getItemCount(): Int {
                return list.size
            }


            override fun updateBinding(binding: ItemBinding, position: Int) {
                val result = list[position]
                binding.model = result
                binding.viewmodel = viewModel

                val imageUri = result.thumbnail.path + "/portrait_medium." + result.thumbnail.extension
                Picasso.with(this@CharacterListActivity).load(imageUri).into(binding.image)
            }
        }

        mainBinding.recycler.adapter = adapter
    }

}
