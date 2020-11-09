package com.example.fitnessapp.activities

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.fitnessapp.R
import com.example.fitnessapp.fragments.CalculateOrEnterMacrosDialog
import com.example.fitnessapp.fragments.RestOrTrainingDialogFragment
import com.example.fitnessapp.services.OnClearFromRecentService
import com.example.fitnessapp.utils.ExpandAnimation
import com.example.fitnessapp.utils.MyPreference
import com.example.fitnessapp.viewmodels.MacrosViewModel
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    lateinit var myPreference: MyPreference
    private val macrosViewModel: MacrosViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        startService(Intent(baseContext, OnClearFromRecentService::class.java))


        myPreference = MyPreference(this)
        val pulsingAnimation = AnimationUtils.loadAnimation(this, R.anim.pulsing)


        if (myPreference.getFirstTimeOpeningApp() == null) {
            text_trainingDay.visibility = View.GONE
            text_restDay.visibility = View.GONE
            circle_calories.visibility = View.GONE
            circle_tapToCalculate.visibility = View.VISIBLE
            circle_carbs.visibility = View.GONE
            circle_fats.visibility = View.GONE
            circle_proteins.visibility = View.GONE

            circle_tapToCalculate.startAnimation(pulsingAnimation)
        }

        if(myPreference.getFirstTimeOpeningApp() != null && myPreference.getTrainingOrRestDay() == null) {
            text_trainingDay.visibility = View.GONE
            text_restDay.visibility = View.GONE
            val restOrTrainingDialogFragment = RestOrTrainingDialogFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val previous = supportFragmentManager.findFragmentByTag("asdasd")
            if (previous != null) {
                fragmentTransaction.remove(previous)
            }
            fragmentTransaction.addToBackStack(null)
            restOrTrainingDialogFragment.show(fragmentTransaction, "asdasd")
        }

        if(myPreference.getTrainingOrRestDay() == "training") {
            text_trainingDay.visibility = View.VISIBLE
            text_restDay.visibility = View.GONE
        } else if(myPreference.getTrainingOrRestDay() == "rest") {
            text_trainingDay.visibility = View.GONE
            text_restDay.visibility = View.VISIBLE
        }




        if (myPreference.getFirstTimeOpeningApp() != null) {
            circle_tapToCalculate.visibility = View.GONE
            circle_calories.visibility = View.VISIBLE
            circle_carbs.visibility = View.VISIBLE
            circle_fats.visibility = View.VISIBLE
            circle_proteins.visibility = View.VISIBLE
            circle_tapToCalculate.clearAnimation()
            showMacroCircles()
        }

        text_restDay.setOnClickListener {
            val restOrTrainingDialogFragment = RestOrTrainingDialogFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val previous = supportFragmentManager.findFragmentByTag("asdasd")
            if (previous != null) {
                fragmentTransaction.remove(previous)
            }
            fragmentTransaction.addToBackStack(null)
            restOrTrainingDialogFragment.show(fragmentTransaction, "asdasd")

        }
        text_trainingDay.setOnClickListener {
            val restOrTrainingDialogFragment = RestOrTrainingDialogFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val previous = supportFragmentManager.findFragmentByTag("asdasd")
            if (previous != null) {
                fragmentTransaction.remove(previous)
            }
            fragmentTransaction.addToBackStack(null)
            restOrTrainingDialogFragment.show(fragmentTransaction, "asdasd")

        }


        circle_calories.setOnClickListener {
            val activityIntent = Intent(this, DailyMacrosIntakeActivity::class.java)
            startActivity(activityIntent)

        }
        macrosSettings.setOnClickListener{
            val calculateOrEnterMacrosFragment = CalculateOrEnterMacrosDialog()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            calculateOrEnterMacrosFragment.show(fragmentTransaction, "asdasdasdasd")
        }

        circle_tapToCalculate.setOnClickListener {
            val calculateOrEnterMacrosFragment = CalculateOrEnterMacrosDialog()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            calculateOrEnterMacrosFragment.show(fragmentTransaction, "asdasdasdasd")


        }

        button_History.setOnClickListener{
            val activityIntent = Intent(this@HomeActivity, MealsHistoryActivity::class.java)
            startActivity(activityIntent)
        }



    }
    fun showMacroCircles() {

        macrosViewModel.getDailyMacroTargets()
        macrosViewModel.getDailyMacroIntake()

        macrosViewModel.dailyMacroTargetsLive.observe(this@HomeActivity, Observer {
            val dailyMacroTargets = it
            macrosViewModel.dailyMacroIntakeLive.observe(
                this@HomeActivity,
                Observer { summedMealMacros ->
                    if (text_trainingDay.visibility == View.VISIBLE) {
                        animateMacroTargetsCircles(
                            dailyMacroTargets.tProtein - summedMealMacros.protein,
                            dailyMacroTargets.tCarbs - summedMealMacros.carbs,
                            dailyMacroTargets.tFats - summedMealMacros.fats
                        )
                    } else {
                        animateMacroTargetsCircles(
                            dailyMacroTargets.rProtein - summedMealMacros.protein,
                            dailyMacroTargets.rCarbs - summedMealMacros.carbs,
                            dailyMacroTargets.rFats - summedMealMacros.fats
                        )
                    }

                })

        })
    }


    @SuppressLint("SetTextI18n")
    fun animateMacroTargetsCircles(proteinTargets: Int, carbsTargets: Int, fatsTargets: Int) {

        val razmernaKonstataNaKrug  = 0.0015015

        val oldProteinMacros:String = if(circle_proteins.text.toString() == "P") {
            "0"

        } else {
            circle_proteins.text.substring(1, circle_proteins.text.length)
        }

        val proteinAnimation = ExpandAnimation(
            circle_proteins,
            circle_proteins.height,
            ((resources.getDimension(R.dimen.min_circle_height_and_width) * razmernaKonstataNaKrug * proteinTargets).toInt()) + (resources.getDimension(
                R.dimen.min_circle_height_and_width
            ).toInt()),
            circle_proteins.width,
            ((resources.getDimension(R.dimen.min_circle_height_and_width) * razmernaKonstataNaKrug * proteinTargets).toInt()) + (resources.getDimension(
                R.dimen.min_circle_height_and_width
            )).toInt()
        )
        proteinAnimation.duration = 1000
        proteinAnimation.fillAfter = true
        circle_proteins.startAnimation(proteinAnimation)

        val proteinAnimator: ValueAnimator
        proteinAnimator = if(circle_proteins.text.toString() == "P") {
            ValueAnimator.ofInt(0, proteinTargets)
        } else {
            ValueAnimator.ofInt(oldProteinMacros.toInt(), proteinTargets)
        }

        proteinAnimator.duration = 1000
        proteinAnimator.addUpdateListener { a -> circle_proteins.text = "P" + a.animatedValue.toString() }
        proteinAnimator.start()

        val oldCarbMacros:String = if(circle_carbs.text.toString() == "C") {
            "0"

        } else {
            circle_carbs.text.substring(1, circle_carbs.text.length)
        }
        val carbAnimation = ExpandAnimation(
            circle_carbs,
            circle_carbs.height,
            (resources.getDimension(R.dimen.min_circle_height_and_width) * razmernaKonstataNaKrug * carbsTargets).toInt() + (resources.getDimension(
                R.dimen.min_circle_height_and_width
            )).toInt(),
            circle_carbs.width,
            (resources.getDimension(R.dimen.min_circle_height_and_width) * razmernaKonstataNaKrug * carbsTargets).toInt() + (resources.getDimension(
                R.dimen.min_circle_height_and_width
            )).toInt()
        )
        carbAnimation.duration = 1000
        carbAnimation.fillAfter = true
        circle_carbs.startAnimation(carbAnimation)

        val carbAnimator: ValueAnimator
        carbAnimator = if(circle_carbs.text.toString() == "C") {
            ValueAnimator.ofInt(0, carbsTargets)
        } else {
            ValueAnimator.ofInt(oldCarbMacros.toInt(), carbsTargets)
        }

        carbAnimator.duration = 1000
        carbAnimator.addUpdateListener { a -> circle_carbs.text = "C" + a.animatedValue.toString() }
        carbAnimator.start()

        val oldFatMacros:String = if(circle_fats.text.toString() == "F") {
            "F"

        } else {
            circle_fats.text.substring(1, circle_fats.text.length)
        }
        val fatAnimation = ExpandAnimation(
            circle_fats,
            circle_fats.height,
            (resources.getDimension(R.dimen.min_circle_height_and_width) * razmernaKonstataNaKrug * fatsTargets).toInt() + (resources.getDimension(
                R.dimen.min_circle_height_and_width
            )).toInt(),
            circle_fats.width,
            (resources.getDimension(R.dimen.min_circle_height_and_width) * razmernaKonstataNaKrug * fatsTargets).toInt() + (resources.getDimension(
                R.dimen.min_circle_height_and_width
            )).toInt()
        )
        fatAnimation.duration = 1000
        fatAnimation.fillAfter = true
        circle_fats.startAnimation(fatAnimation)

        val fatAnimator: ValueAnimator
        fatAnimator = if(circle_fats.text.toString() == "F") {
            ValueAnimator.ofInt(0, fatsTargets)
        } else {
            ValueAnimator.ofInt(oldFatMacros.toInt(), fatsTargets)
        }

        fatAnimator.duration = 1000
        fatAnimator.addUpdateListener { a -> circle_fats.text = "F" + a.animatedValue.toString() }
        fatAnimator.start()

        val calories = (proteinTargets *4) + (carbsTargets *4) + (fatsTargets * 9)
        circle_calories.text = "$calories\nCal"

    }

    override fun onResume() {
        super.onResume()
        if(myPreference.getFirstTimeOpeningApp()!= null) {
            circle_calories.visibility = View.VISIBLE
            circle_tapToCalculate.visibility = View.GONE
            circle_carbs.visibility = View.VISIBLE
            circle_fats.visibility = View.VISIBLE
            circle_proteins.visibility = View.VISIBLE
            showMacroCircles()

        }

    }



    fun setRestDayTitle() {
        text_restDay.visibility = View.VISIBLE
        text_trainingDay.visibility = View.GONE
    }

    fun setTrainingDayTitle() {
        text_restDay.visibility = View.GONE
        text_trainingDay.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }

}




