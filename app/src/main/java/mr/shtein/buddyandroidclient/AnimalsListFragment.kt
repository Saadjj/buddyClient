package mr.shtein.buddyandroidclient

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import mr.shtein.buddyandroidclient.adapters.AnimalsAdapter
import mr.shtein.buddyandroidclient.model.Animal
import mr.shtein.buddyandroidclient.model.AnimalType
import mr.shtein.buddyandroidclient.retrofit.Common
import mr.shtein.buddyandroidclient.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.TypedValue

import android.content.res.Resources
import android.view.*
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import mr.shtein.buddyandroidclient.adapters.OnAnimalCardClickListener


class AnimalsListFragment : Fragment(), OnAnimalCardClickListener {

    lateinit var mService: RetrofitServices
    lateinit var adapter: AnimalsAdapter
    lateinit var animalRecyclerView: RecyclerView
    private val ROLE_KEY: String = "role"
    private val PERSISTANT_STORAGE_NAME: String = "buddy_storage"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.animals_list_fragment, container, false)
        val navController = findNavController()
        val bottomBar = view.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomBar.setOnItemSelectedListener {
            val store: SharedPreferences? = context?.getSharedPreferences(PERSISTANT_STORAGE_NAME, Context.MODE_PRIVATE)
            var role: String? = ""
            if (store != null) {
                role = store.getString(ROLE_KEY, null)
            }
            if (role == null) {
                navController.navigate(R.id.action_animal_choice_fragment_to_profileFragment2)
            } else {
                Toast.makeText(context, "Store is not empty", Toast.LENGTH_LONG).show()
            }
            return@setOnItemSelectedListener true
        }
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mService = Common.retrofitService
        getAnimalTypesAndDoChips(view)
        animalRecyclerView = view.findViewById(R.id.animal_list)
        animalRecyclerView.setHasFixedSize(true);
        animalRecyclerView.layoutManager = LinearLayoutManager(context)
        getAllAnimalsList(view)
    }

    override fun onStart() {
        super.onStart()
        changeStatusBar(requireActivity(), R.color.white)
    }

    override fun onStop() {
        super.onStop()
        changeStatusBar(requireActivity(), R.color.end_of_main_gradient)
    }

    private fun changeStatusBar(activity: Activity, color: Int) {
        activity.window.statusBarColor = requireContext().getColor(color)
        val windowInsetController: WindowInsetsControllerCompat? =
            ViewCompat.getWindowInsetsController(requireActivity().window.decorView)
        val changeTo = windowInsetController?.isAppearanceLightStatusBars
        windowInsetController?.isAppearanceLightStatusBars = !changeTo!!
    }

    private fun getAnimalTypesAndDoChips(view: View) {
        mService
            .getAnimalTypes()
            .enqueue(object : Callback<MutableList<AnimalType>> {
                override fun onResponse(
                    call: Call<MutableList<AnimalType>>,
                    response: Response<MutableList<AnimalType>>
                ) {
                    val typeList: MutableList<AnimalType> =
                        response.body() as MutableList<AnimalType>
                    val typeChipGroup: ChipGroup = view.findViewById(R.id.animal_choice_chips)
                    for (type in typeList) {
                        val topAndDownPadding = dpToPx(16, view)
                        val startAndStopPadding = dpToPx(32, view)

                        val curChip: Chip = Chip(view.context)
                        curChip.text = type.pluralAnimalType
                        curChip.chipBackgroundColor =
                            context?.getColorStateList(R.color.choice_color)
                        curChip.setPadding(
                            startAndStopPadding,
                            topAndDownPadding,
                            startAndStopPadding,
                            topAndDownPadding
                        )
                        typeChipGroup.addView(curChip)
                    }
                }

                override fun onFailure(call: Call<MutableList<AnimalType>>, t: Throwable) {
                    t.message?.let { Log.d("Animal", it) }
                }
            })
    }

    override fun onAnimalCardClick(animalId: Long) {
        val bundle = Bundle()
        bundle.putLong("animalId", animalId)
        findNavController().navigate(R.id.animalsCardFragment, bundle)
    }

    private fun dpToPx(dp: Int, view: View): Int {
        val r: Resources = view.context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        ).toInt()
    }

    private fun getAllAnimalsList(view: View) {
        mService.getAnimals().enqueue(object : Callback<MutableList<Animal>> {
            override fun onFailure(call: Call<MutableList<Animal>>, t: Throwable) {
                t.message?.let { Log.d("Animal", it) }
            }

            override fun onResponse(
                call: Call<MutableList<Animal>>,
                response: Response<MutableList<Animal>>
            ) {
                Log.d("Animal", "onResponse is ready")
                adapter = AnimalsAdapter(
                    requireContext(),
                    response.body() as MutableList<Animal>,
                    this@AnimalsListFragment
                )
                adapter.notifyDataSetChanged()
                view.findViewById<ProgressBar>(R.id.animal_search_progress).visibility =
                    View.INVISIBLE
                view.findViewById<ImageView>(R.id.logo_in_animal_list).visibility = View.INVISIBLE
                animalRecyclerView.adapter = adapter
            }
        })
    }

    private fun checkRole(): String {
        val preferences =
            context?.getSharedPreferences(PERSISTANT_STORAGE_NAME, Context.MODE_PRIVATE)
        return preferences?.getString(ROLE_KEY, "") ?: ""
    }

}