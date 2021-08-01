package us.martypants.marveldemo.ux

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import us.martypants.marvel.App
import us.martypants.marveldemo.R
import us.martypants.marveldemo.databinding.ActivityCharacterDetailBinding
import us.martypants.marveldemo.util.MyViewModelFactory

class CharacterDetailActivity : AppCompatActivity() {


    private lateinit var viewModel: CharacterViewModel
    private lateinit var detailBinding: ActivityCharacterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        App.app?.userComponent?.inject(this)
        viewModel =
            ViewModelProviders.of(this, MyViewModelFactory(application as App))
                .get(CharacterViewModel::class.java)

        if (intent.hasExtra(DETAIL)) {

            viewModel.getCharacterDetail(intent.getIntExtra(DETAIL, 0))
            setupObservers()
        }
    }

    private fun setupObservers() {
        // Data
        viewModel.characterDetail.observe(this, Observer {
            it?.let {
                detailBinding.model = it
                val imageUri = it.thumbnail.path + "/portrait_uncanny." + it.thumbnail.extension
                Picasso.with(this).load(imageUri).into(detailBinding.image)

            }
        })

        // Events
    }

    companion object {
        const val DETAIL = "detail"
    }
}