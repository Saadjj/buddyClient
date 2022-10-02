package mr.shtein.buddyandroidclient.presentation.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialSharedAxis
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import mr.shtein.buddyandroidclient.R
import mr.shtein.buddyandroidclient.adapters.SelectionAdapter
import mr.shtein.buddyandroidclient.databinding.AnimalFilterFragmentBinding
import mr.shtein.buddyandroidclient.model.dto.FilterAutocompleteItem
import mr.shtein.buddyandroidclient.presentation.presenter.AnimalFilterPresenter
import mr.shtein.buddyandroidclient.utils.FragmentsListForAssigningAnimation
import org.koin.android.ext.android.get

private const val ANIMAL_FILTER_KEY = "animal_filter"
private const val LAST_FRAGMENT_KEY = "last_fragment"

class AnimalFilterFragment : MvpAppCompatFragment(), AnimalFilterView {

    private var _binding: AnimalFilterFragmentBinding? = null
    private lateinit var breedsAdapter: SelectionAdapter
    private lateinit var colorsAdapter: SelectionAdapter
    private lateinit var citiesAdapter: SelectionAdapter
    private lateinit var typesAdapter: SelectionAdapter
    private val binding get() = _binding!!
    var count = 0

    @InjectPresenter
    lateinit var animalFilterPresenter: AnimalFilterPresenter

    @ProvidePresenter
    fun providePresenter(): AnimalFilterPresenter {
        return get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AnimalFilterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setUpTransitions() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun initAdapters(
        animalBreeds: List<FilterAutocompleteItem>,
        animalColors: List<FilterAutocompleteItem>,
        animalTypes: List<FilterAutocompleteItem>,
        animalCities: List<FilterAutocompleteItem>
    ) {
        breedsAdapter = SelectionAdapter(requireContext(), animalBreeds)
        binding.animalFilterBreedInput.setAdapter(breedsAdapter)
        binding.animalFilterBreedInput.setDropDownBackgroundResource(R.color.white)

        colorsAdapter = SelectionAdapter(requireContext(), animalColors)
        binding.animalFilterColorInput.setAdapter(colorsAdapter)
        binding.animalFilterColorInput.setDropDownBackgroundResource(R.color.white)

        citiesAdapter = SelectionAdapter(requireContext(), animalCities)
        binding.animalFilterCityInput.setAdapter(citiesAdapter)
        binding.animalFilterCityInput.setDropDownBackgroundResource(R.color.white)

        typesAdapter = SelectionAdapter(requireContext(), animalTypes)
        binding.animalFilterAnimalTypeInput.setAdapter(typesAdapter)
        binding.animalFilterAnimalTypeInput.setDropDownBackgroundResource(R.color.white)

    }

    override fun closeKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun showBreedChips(breedsForChips: MutableList<FilterAutocompleteItem>) {
        val breedChips: List<Chip> = makeBreedChips(breedsForChips)
        breedChips.forEach {
            binding.animalFilterBreedChipsContainer.addView(it)
        }
    }


    override fun showColorChips(colorsForChips: MutableList<FilterAutocompleteItem>) {
        val colorChips: List<Chip> = makeColorChips(colorsForChips)
        colorChips.forEach {
            binding.animalFilterColorChipsContainer.addView(it)
        }
    }

    override fun showAnimalTypeChips(animalTypesForChips: MutableList<FilterAutocompleteItem>) {
        val animalTypeChips: List<Chip> = makeAnimalTypeChips(animalTypesForChips)
        animalTypeChips.forEach {
            binding.animalFilterAnimalTypeChipsContainer.addView(it)
        }
    }

    override fun showCityChips(citiesForChips: MutableList<FilterAutocompleteItem>) {
        val citiesChips: List<Chip> = makeCityChips(citiesForChips)
        citiesChips.forEach {
            binding.animalFilterCityChipsContainer.addView(it)
        }
    }

    override fun setListeners() {
        binding.animalFilterBreedInput.setOnItemClickListener { parent, view, position, id ->

            val adapter = binding.animalFilterBreedInput.adapter as SelectionAdapter
            val filterBreed = adapter.getItem(position)
            animalFilterPresenter.onBreedFilterItemClick(filterBreed!!)
            filterBreed.isSelected = true
            filterBreed.let {
                val chip = makeBreedChip(it.name, it.id)
                binding.animalFilterBreedChipsContainer.addView(chip)
            }
            binding.animalFilterBreedInput.setText("")
        }

        binding.animalFilterColorInput.setOnItemClickListener { parent, view, position, id ->

            val adapter = binding.animalFilterColorInput.adapter as SelectionAdapter
            val filterColor = adapter.getItem(position)
            animalFilterPresenter.onColorFilterItemClick(filterColor!!)
            filterColor.isSelected = true
            filterColor.let {
                val chip = makeColorChip(it.name, it.id)
                binding.animalFilterColorChipsContainer.addView(chip)
            }
            binding.animalFilterColorInput.setText("")
        }

        binding.animalFilterCityInput.setOnItemClickListener { parent, view, position, id ->

            val adapter = binding.animalFilterCityInput.adapter as SelectionAdapter
            val filterCity = adapter.getItem(position)
            animalFilterPresenter.onCityFilterItemClick(filterCity!!)
            filterCity.isSelected = true
            filterCity.let {
                val chip = makeCityChip(it.name, it.id)
                binding.animalFilterCityChipsContainer.addView(chip)
            }
            binding.animalFilterCityInput.setText("")
        }

        binding.animalFilterAnimalTypeInput.setOnItemClickListener { parent, view, position, id ->

            val adapter = binding.animalFilterAnimalTypeInput.adapter as SelectionAdapter
            val filterAnimalType = adapter.getItem(position)
            animalFilterPresenter.onAnimalTypeFilterItemClick(filterAnimalType!!)
            filterAnimalType.isSelected = true
            filterAnimalType.let {
                val chip = makeAnimalTypeChip(it.name, it.id)
                binding.animalFilterAnimalTypeChipsContainer.addView(chip)
            }
            binding.animalFilterAnimalTypeInput.setText("")
        }

        binding.animalFilterFindBtn.setOnClickListener {
            val bundle =
                bundleOf(LAST_FRAGMENT_KEY to FragmentsListForAssigningAnimation.ANIMAL_FILTER)
            findNavController().navigate(
                R.id.action_animalFilterFragment_to_animalsListFragment,
                bundle
            )
        }
    }

    override fun updateBtnValue(animalAfterFilteredCount: Int) {
        binding.animalFilterFindBtn.text = resources.getQuantityString(
            R.plurals.filter_btn_animal_count,
            animalAfterFilteredCount,
            animalAfterFilteredCount
        )
    }

    override fun deleteBreedChip(chip: Chip) {
        binding.animalFilterBreedChipsContainer.removeView(chip)
    }

    override fun updateBreedList(breeds: List<FilterAutocompleteItem>?) {
        val adapter = binding.animalFilterBreedInput.adapter as SelectionAdapter
        adapter.clear()
        adapter.addAll(breeds!!)
        adapter.notifyDataSetChanged()
    }

    override fun deleteColorChip(chip: Chip) {
        binding.animalFilterColorChipsContainer.removeView(chip)
    }

    override fun updateColorList(colors: List<FilterAutocompleteItem>?) {
        val adapter = binding.animalFilterColorInput.adapter as SelectionAdapter
        adapter.clear()
        adapter.addAll(colors!!)
        adapter.notifyDataSetChanged()
    }

    override fun deleteCityChip(chip: Chip) {
        binding.animalFilterCityChipsContainer.removeView(chip)
    }

    override fun updateCityList(cities: List<FilterAutocompleteItem>?) {
        val adapter = binding.animalFilterCityInput.adapter as SelectionAdapter
        adapter.clear()
        adapter.addAll(cities!!)
        adapter.notifyDataSetChanged()
    }

    override fun deleteAnimalTypeChip(chip: Chip) {
        binding.animalFilterAnimalTypeChipsContainer.removeView(chip)
    }

    override fun updateAnimalTypeList(types: List<FilterAutocompleteItem>?) {
        val adapter = binding.animalFilterAnimalTypeInput.adapter as SelectionAdapter
        adapter.clear()
        adapter.addAll(types!!)
        adapter.notifyDataSetChanged()
    }

    private fun makeBreedChips(breedsForChips: MutableList<FilterAutocompleteItem>): List<Chip> {
        return breedsForChips
            .map {
                makeBreedChip(it.name, it.id)
            }
            .toList()
    }

    private fun makeColorChips(colorsForChips: MutableList<FilterAutocompleteItem>): List<Chip> {
        return colorsForChips
            .map {
                makeColorChip(it.name, it.id)
            }
            .toList()
    }

    private fun makeCityChips(citiesForChips: MutableList<FilterAutocompleteItem>): List<Chip> {
        return citiesForChips
            .map {
                makeCityChip(it.name, it.id)
            }
            .toList()
    }

    private fun makeAnimalTypeChips(animalTypesForChips: MutableList<FilterAutocompleteItem>): List<Chip> {
        return animalTypesForChips
            .map {
                makeAnimalTypeChip(it.name, it.id)
            }
            .toList()
    }

    private fun makeBreedChip(text: String, itemId: Int): Chip {
        val chip = layoutInflater.inflate(
            R.layout.filter_item_chip   ,
            binding.animalFilterBreedChipsContainer,
            false
        ) as Chip
        chip.text = text
        chip.tag = itemId
        chip.setOnClickListener {
            animalFilterPresenter.onBreedChipCloseBtnClicked(it as Chip)
        }
        return chip
    }

    private fun makeColorChip(text: String, itemId: Int): Chip {
        val chip = layoutInflater.inflate(
            R.layout.filter_item_chip   ,
            binding.animalFilterColorChipsContainer,
            false
        ) as Chip
        chip.text = text
        chip.tag = itemId
        chip.setOnClickListener {
            animalFilterPresenter.onColorChipCloseBtnClicked(it as Chip)
        }
        return chip
    }

    private fun makeCityChip(text: String, itemId: Int): Chip {
        val chip = layoutInflater.inflate(
            R.layout.filter_item_chip   ,
            binding.animalFilterCityChipsContainer,
            false
        ) as Chip
        chip.text = text
        chip.tag = itemId
        chip.setOnClickListener {
            animalFilterPresenter.onCityChipCloseBtnClicked(it as Chip)
        }
        return chip
    }

    private fun makeAnimalTypeChip(text: String, itemId: Int): Chip {
        val chip = layoutInflater.inflate(
            R.layout.filter_item_chip   ,
            binding.animalFilterAnimalTypeChipsContainer,
            false
        ) as Chip
        chip.text = text
        chip.tag = itemId
        chip.setOnClickListener {
            animalFilterPresenter.onAnimalTypeChipCloseBtnClicked(it as Chip)
        }
        return chip
    }
}