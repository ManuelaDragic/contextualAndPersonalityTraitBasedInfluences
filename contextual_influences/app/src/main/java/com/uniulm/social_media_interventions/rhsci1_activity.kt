package com.uniulm.social_media_interventions

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import com.uniulm.social_media_interventions.*
import kotlinx.android.synthetic.main.activity_after_rshci.*
import kotlinx.android.synthetic.main.activity_after_rshci1.*
import kotlinx.android.synthetic.main.activity_after_rshci1.view.*
import kotlinx.android.synthetic.main.activity_sess_feeling_after.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.random.Random
import android.widget.TextView

class rhsci1_activity : AppCompatActivity() {
    var interventionType = ""
    var timestamp = ""
    var appName = ""
    val question = "How do you feel after the session?"
    private lateinit var cardView1: CardView
    private lateinit var cardView2: CardView
    private lateinit var linearLayout: LinearLayout
    var check0 = false
    var check1 = false
    var check2 = false
    var check3 = false
    var check4 = false
    var check5 = false
    var check6 = false
    var check11 = false
    var checkKSS = false
    var checkAtHome = false
    var checkSam = false
    var checkStress = false
    var checkSenseOfAgency = false
    var checkSatisfaction = false
    var checkGoalAlignment = false
    var checkUsefulInSituation = false
    var answer6 = "false"
    var answer0 = "false"
    var pID = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_rshci1)

        cardView1 = findViewById(R.id.cardack1)
        cardView2 = findViewById(R.id.cardack2)

        linearLayout = findViewById(R.id.linearLayout)

        // remove attention check to place new
        linearLayout.removeView(cardView1)
        linearLayout.removeView(cardView2)

        //randomly place ack
        placeAckRandomly()

        for (index in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(index)
            Log.d("ChildIndex", "Index: $index, ID: ${child.id}, Type: ${child.javaClass.simpleName}")
        }


        val startBtn: Button = findViewById(R.id.startBtn)
        startBtn.setOnClickListener {
            checkGroup1()
            //sendData()
        }

    }

    // Place the attention checks randomly
    private fun placeAckRandomly() {

        val allowedPositions = listOf(4, 5, 6, 7, 8, 9, 10, 11, 12, 16, 17)
        val randomIndex = allowedPositions[Random.nextInt(allowedPositions.size)]
        Log.d("RandomIndex", "Chosen random index for Attention Check: $randomIndex")


        // Entscheidet, welcher Attention Check angezeigt wird
        val whichCard = Random.nextInt(3)

        when (whichCard) {
            0 -> {
                // Zeige keinen der Attention Checks
                Log.d("RandomIndex", "No attention check will be shown")
                cardView1.visibility = View.GONE
                cardView2.visibility = View.GONE
            }
            1 -> {
                // Zeige nur cardView1 (ack1)
                linearLayout.addView(cardView1, randomIndex)
                cardView1.visibility = View.VISIBLE
                cardView2.visibility = View.GONE
                Log.d("RandomIndex", "Showing cardView1 at index $randomIndex")
            }
            2 -> {
                // Zeige nur cardView2 (ack2)
                linearLayout.addView(cardView2, randomIndex)
                cardView1.visibility = View.GONE
                cardView2.visibility = View.VISIBLE
                Log.d("RandomIndex", "Showing cardView2 at index $randomIndex")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }


    fun gon() {
        val intent = Intent(this, ThankActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    fun sendQMWarning() {
        Toast.makeText(
            this,
            "At least one question is missing", Toast.LENGTH_SHORT
        ).show()
    }

    /*----- Attention checks -----*/

    // cardView1
    // "Attention Check: I swim across the atlantic ocean every day to get to work"
    fun checkGroup0() {
        answer0 = "false"
        check0 = false
        if (cardView1.isVisible == true) {

            if (rad54.isChecked) {
                answer0 = "false"
                saveres_ack(answer0, 0)
                check0 = true
            }
            else if (rad53.isChecked) {
                answer0 = "false"
                saveres_ack(answer0, 0)
                check0 = true
            }
            else if (rad52.isChecked) {
                answer0 = "false"
                saveres_ack(answer0, 0)
                check0 = true
            }
            else if (rad51.isChecked) {
                answer0 = "true"
                saveres_ack(answer0, 0)
                check0 = true
            }
            else if (rad50.isChecked) {
                answer0 = "true"
                saveres_ack(answer0, 0)
                check0 = true
            }
            else {
                sendQMWarning()
            }

            // if checked, send data
            if (check0) {
                sendData()
            }
        }
    }

    // cardView2
    // "Attention Check: I breathe in more than once a day"
    fun checkGroup6() {
        answer0 = "false"
        check6 = false
        if (cardView2.isVisible == true) {

            if (radack2_1.isChecked) {
                answer0 = "false"
                saveres_ack(answer0, 6)
                check6 = true
            }
            else if (radack2_2.isChecked) {
                answer0 = "false"
                saveres_ack(answer0, 6)
                check6 = true
            }
            else if (radack2_3.isChecked) {
                answer0 = "false"
                saveres_ack(answer0, 6)
                check6 = true
            }
            else if (radack2_4.isChecked) {
                answer0 = "true"
                saveres_ack(answer0, 6)
                check6 = true
            }
            else if (radack2_5.isChecked) {
                answer0 = "true"
                saveres_ack(answer0, 6)
                check6 = true
            }
            else {
                sendQMWarning()
            }

            // if checked, send data
            if (check6) {
                sendData()

            }
        }
    }

    /*----- "Refer to the itervention you received" -----*/

    // "1. I want to be in control not my phone"
    fun checkGroup1() {
        var answer1 = 42
        check1 = false
        if (rad01.isChecked) {
            answer1 = 5
            saveres(answer1, 1)
            check1 = true
        } else if (rad02.isChecked) {
            answer1 = 4
            saveres(answer1, 1)
            check1 = true
        } else if (rad03.isChecked) {
            answer1 = 3
            saveres(answer1, 1)
            check1 = true
        } else if (rad04.isChecked) {
            answer1 = 2
            saveres(answer1, 1)
            check1 = true
        } else if (rad05.isChecked) {
            answer1 = 1
            saveres(answer1, 1)
            check1 = true
        } else {
            sendQMWarning()
        }

        if (check1) {
            checkGroup2()
        }
    }

    // "2. I like to act independently from my phone"
    fun checkGroup2() {
        var answer2 = 42
        check2 = false
        if (rad11.isChecked) {
            answer2 = 5
            saveres(answer2, 2)
            check2 = true
        } else if (rad12.isChecked) {
            answer2 = 4
            saveres(answer2, 2)
            check2 = true
        } else if (rad13.isChecked) {
            answer2 = 3
            saveres(answer2, 2)
            check2 = true
        } else if (rad14.isChecked) {
            answer2 = 2
            saveres(answer2, 2)
            check2 = true
        } else if (rad15.isChecked) {
            answer2 = 1
            saveres(answer2, 2)
            check2 = true
        } else {
            sendQMWarning()
        }

        if (check2) {
            checkGroup3()
            //checkGroupKSS()
        }
    }

    // "3. i don´t want to tell my phone what to do"
    fun checkGroup3() {
        var answer3 = 42
        check3 = false
        if (rad21.isChecked) {
            answer3 = 5
            saveres(answer3, 3)
            check3 = true
        } else if (rad22.isChecked) {
            answer3 = 4
            saveres(answer3, 3)
            check3 = true
        } else if (rad23.isChecked) {
            answer3 = 3
            saveres(answer3, 3)
            check3 = true
        } else if (rad24.isChecked) {
            answer3 = 2
            saveres(answer3, 3)
            check3 = true
        } else if (rad25.isChecked) {
            answer3 = 1
            saveres(answer3, 3)
            check3 = true
        } else {
            sendQMWarning()
        }
        if (check3) {
            checkGroup4()
        }
    }

    // "4. I don´t let my phone impose it´s will on me"
    fun checkGroup4() {
        var answer4 = 42
        check4 = false
        if (rad31.isChecked) {
            answer4 = 5
            saveres(answer4, 4)
            check4 = true
        } else if (rad32.isChecked) {
            answer4 = 4
            saveres(answer4, 4)
            check4 = true
        } else if (rad33.isChecked) {
            answer4 = 3
            saveres(answer4, 4)
            check4 = true
        } else if (rad34.isChecked) {
            answer4 = 2
            saveres(answer4, 4)
            check4 = true
        } else if (rad35.isChecked) {
            answer4 = 1
            saveres(answer4, 4)
            check4 = true
        } else {
            sendQMWarning()
        }
        if (check4) {
            checkGroup5()
        }
    }

    // "5. I alone determine what I do, not my phone"
    fun checkGroup5() {
        var answer5 = 42
        check5 = false
        if (rad41.isChecked) {
            answer5 = 5
            saveres(answer5, 5)
            check5 = true
        } else if (rad42.isChecked) {
            answer5 = 4
            saveres(answer5, 5)
            check5 = true
        } else if (rad43.isChecked) {
            answer5 = 3
            saveres(answer5, 5)
            check5 = true
        } else if (rad44.isChecked) {
            answer5 = 2
            saveres(answer5, 5)
            check5 = true
        } else if (rad45.isChecked) {
            answer5 = 1
            saveres(answer5, 5)
            check5 = true
        }
        else {
            sendQMWarning()
        }
        /*
                if (check5){
                    if(cardView1.isVisible==true) {
                        Log.e("QUESTIONNAIRE", "cardView1visibile-  checkgroup 5")
                        checkGroup0()
                    }else if(cardView2.isVisible==true){
                        Log.e("QUESTIONNAIRE", "cardView1notvisibile-  checkgroup 5")
                        checkGroup6()
                    }
                }*/
        if (check5) {
            checkGroupSenseOfAgency()
            // checkGroup9()
        }
    }

    // "6. For this intervention, how much did you feel out of or in control"
    fun checkGroupSenseOfAgency() {
        var answerSenseOfAgency = 42
        checkSenseOfAgency = false
        if (r_sense_of_agency1.isChecked) {
            answerSenseOfAgency = 1
            saveres(answerSenseOfAgency, 6)
            checkSenseOfAgency = true
        } else if (r_sense_of_agency2.isChecked) {
            answerSenseOfAgency = 2
            saveres(answerSenseOfAgency, 6)
            checkSenseOfAgency = true
        } else if (r_sense_of_agency3.isChecked) {
            answerSenseOfAgency = 3
            saveres(answerSenseOfAgency, 6)
            checkSenseOfAgency = true
        } else if (r_sense_of_agency4.isChecked) {
            answerSenseOfAgency = 4
            saveres(answerSenseOfAgency, 6)
            checkSenseOfAgency = true
        } else if (r_sense_of_agency5.isChecked) {
            answerSenseOfAgency = 5
            saveres(answerSenseOfAgency, 6)
            checkSenseOfAgency = true
        } else if (r_sense_of_agency6.isChecked) {
            answerSenseOfAgency = 6
            saveres(answerSenseOfAgency, 6)
            checkSenseOfAgency = true
        } else if (r_sense_of_agency7.isChecked) {
            answerSenseOfAgency = 7
            saveres(answerSenseOfAgency, 6)
            checkSenseOfAgency = true
        }
        else {
            sendQMWarning()
        }
        if (checkSenseOfAgency) {
            checkGroupSatisfaction()
        }
    }

    // "7. For this intervention, how much did you feel dissatisfied or satisfied"
    fun checkGroupSatisfaction() {
        var answerSatisfaction = 42
        checkSatisfaction = false
        if (r_satisfaction1.isChecked) {
            answerSatisfaction = 1
            saveres(answerSatisfaction, 7)
            checkSatisfaction = true
        } else if (r_satisfaction2.isChecked) {
            answerSatisfaction = 2
            saveres(answerSatisfaction, 7)
            checkSatisfaction = true
        } else if (r_satisfaction3.isChecked) {
            answerSatisfaction = 3
            saveres(answerSatisfaction, 7)
            checkSatisfaction = true
        } else if (r_satisfaction4.isChecked) {
            answerSatisfaction = 4
            saveres(answerSatisfaction, 7)
            checkSatisfaction = true
        } else if (r_satisfaction5.isChecked) {
            answerSatisfaction = 5
            saveres(answerSatisfaction, 7)
            checkSatisfaction = true
        } else if (r_satisfaction6.isChecked) {
            answerSatisfaction = 6
            saveres(answerSatisfaction, 7)
            checkSatisfaction = true
        } else if (r_satisfaction7.isChecked) {
            answerSatisfaction = 7
            saveres(answerSatisfaction, 7)
            checkSatisfaction = true
        }
        else {
            sendQMWarning()
        }
        if (checkSatisfaction) {
            checkGroupGoalAlignment()
        }
    }

    // "8. For this intervention, how much did it conflict with or support your personal goals"
    fun checkGroupGoalAlignment() {
        var answerGoalAlignment = 42
        checkGoalAlignment = false
        if (r_goal_alignment1.isChecked) {
            answerGoalAlignment = 1
            saveres(answerGoalAlignment, 8)
            checkGoalAlignment = true
        } else if (r_goal_alignment2.isChecked) {
            answerGoalAlignment = 2
            saveres(answerGoalAlignment, 8)
            checkGoalAlignment = true
        } else if (r_goal_alignment3.isChecked) {
            answerGoalAlignment = 3
            saveres(answerGoalAlignment, 8)
            checkGoalAlignment = true
        } else if (r_goal_alignment4.isChecked) {
            answerGoalAlignment = 4
            saveres(answerGoalAlignment, 8)
            checkGoalAlignment = true
        } else if (r_goal_alignment5.isChecked) {
            answerGoalAlignment = 5
            saveres(answerGoalAlignment, 8)
            checkGoalAlignment = true
        } else if (r_goal_alignment6.isChecked) {
            answerGoalAlignment = 6
            saveres(answerGoalAlignment, 8)
            checkGoalAlignment = true
        } else if (r_goal_alignment7.isChecked) {
            answerGoalAlignment = 7
            saveres(answerGoalAlignment, 8)
            checkGoalAlignment = true
        }
        else {
            sendQMWarning()
        }
        if (checkGoalAlignment) {
            checkGroupUsefulInSituation()
        }
    }

    // "9. I ﬁnd the intervention to be useful in my current situation"
    fun checkGroupUsefulInSituation() {
        var answerUsefulInSituation = 42
        checkUsefulInSituation = false
        if (r_useful_in_situation1.isChecked) {
            answerUsefulInSituation = 1
            saveres(answerUsefulInSituation, 111)
            checkUsefulInSituation = true
        } else if (r_useful_in_situation2.isChecked) {
            answerUsefulInSituation = 2
            saveres(answerUsefulInSituation, 111)
            checkUsefulInSituation = true
        } else if (r_useful_in_situation3.isChecked) {
            answerUsefulInSituation = 3
            saveres(answerUsefulInSituation, 111)
            checkUsefulInSituation = true
        } else if (r_useful_in_situation4.isChecked) {
            answerUsefulInSituation = 4
            saveres(answerUsefulInSituation, 111)
            checkUsefulInSituation = true
        } else if (r_useful_in_situation5.isChecked) {
            answerUsefulInSituation = 5
            saveres(answerUsefulInSituation, 111)
            checkUsefulInSituation = true
        } else if (r_useful_in_situation6.isChecked) {
            answerUsefulInSituation = 6
            saveres(answerUsefulInSituation, 111)
            checkUsefulInSituation = true
        } else if (r_useful_in_situation7.isChecked) {
            answerUsefulInSituation = 7
            saveres(answerUsefulInSituation, 111)
            checkUsefulInSituation = true
        }
        else {
            sendQMWarning()
        }
        if (checkUsefulInSituation) {
            checkGroupSam()
        }
    }


    /*----- "What is your current context" -----*/

    // "2.1 How do you feel"
    fun checkGroupSam() {
        var answerSam = 404
        checkSam = false
        if (sam1.isChecked) {
            answerSam = 1
            saveres(answerSam, 333)
            checkSam = true
        } else if (sam2.isChecked) {
            answerSam = 2
            saveres(answerSam, 333)
            checkSam = true
        } else if (sam3.isChecked) {
            answerSam = 3
            saveres(answerSam, 333)
            checkSam = true
        } else if (sam4.isChecked) {
            answerSam = 4
            saveres(answerSam, 333)
            checkSam = true
        } else if (sam5.isChecked) {
            answerSam = 5
            saveres(answerSam, 333)
            checkSam = true
        } else {
            sendQMWarning()
        }
        if (checkSam) {
            checkGroup9()
            // checkGroup3()
        }
    }

    // "2.2 What is your current activity"
    fun checkGroup9() {
        var answer9 = ""
        var check9 = false

        if (rs1.isChecked) {
            answer9 = "-3"
            saveres(answer9, 9)
            check9 = true
        } else if (rs2.isChecked) {
            answer9 = "-2"
            saveres(answer9, 9)
            check9 = true
        } else if (rs3.isChecked) {
            answer9 = "-1"
            saveres(answer9, 9)
            check9 = true
        } else if (rs4.isChecked) {
            answer9 = "0"
            saveres(answer9, 9)
            check9 = true
        } else if (rs5.isChecked) {
            answer9 = "1"
            saveres(answer9, 9)
            check9 = true
        } else if (rs6.isChecked) {
            answer9 = "2"
            saveres(answer9, 9)
            check9 = true
        } else if (rs7.isChecked) {
            answer9 = "3"
            saveres(answer9, 9)
            check9 = true
        } else {
            sendQMWarning()
        }
        if (check9) {
            checkGroupStress()
        }
    }

    // "2.3 What number best describes your level of stress right now?"
    fun checkGroupStress() {
        var answerStress = 42
        checkStress = false

        if (r_stress0.isChecked) {
            answerStress = 0
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress1.isChecked) {
            answerStress = 1
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress2.isChecked) {
            answerStress = 2
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress3.isChecked) {
            answerStress = 3
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress4.isChecked) {
            answerStress = 4
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress5.isChecked) {
            answerStress = 5
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress6.isChecked) {
            answerStress = 6
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress7.isChecked) {
            answerStress = 7
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress8.isChecked) {
            answerStress = 8
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress9.isChecked) {
            answerStress = 9
            saveres(answerStress, 12)
            checkStress = true
        } else if (r_stress10.isChecked) {
            answerStress = 10
            saveres(answerStress, 12)
            checkStress = true
        } else {
            sendQMWarning()
        }
        if (checkStress) {
            checkGroupKSS()
        }
    }



    // "2.4 What is your level of sleepiness"
    fun checkGroupKSS() {
        var answerKSS = 100
        checkKSS = false
        if (kss1.isChecked) {
            answerKSS = 1
            saveres(answerKSS, 230)
            checkKSS = true
        } else if (kss2.isChecked) {
            answerKSS = 2
            saveres(answerKSS, 230)
            checkKSS = true
        } else if (kss3.isChecked) {
            answerKSS = 3
            saveres(answerKSS, 230)
            checkKSS = true
        } else if (kss4.isChecked) {
            answerKSS = 4
            saveres(answerKSS, 230)
            checkKSS = true
        } else if (kss5.isChecked) {
            answerKSS = 5
            saveres(answerKSS, 230)
            checkKSS = true
        } else if (kss6.isChecked) {
            answerKSS = 6
            saveres(answerKSS, 230)
            checkKSS = true
        } else if (kss7.isChecked) {
            answerKSS = 7
            saveres(answerKSS, 230)
            checkKSS = true
        } else if (kss8.isChecked) {
            answerKSS = 8
            saveres(answerKSS, 230)
            checkKSS = true
        } else if (kss9.isChecked) {
            answerKSS = 9
            saveres(answerKSS, 230)
            checkKSS = true
        } else {
            sendQMWarning()
        }
        if (checkKSS) {
            checkGroup11()
        }
    }

    // "2.5 Which one of these best describes people around you"
    fun checkGroup11() {
        var answer11 = ""
        check11 = false
        if (alone.isChecked) {
            answer11 = "alone"
            saveres(answer11, 11)
            check11 = true
        } else if (friends.isChecked) {
            answer11 = "friends"
            saveres(answer11, 11)
            check11 = true
        }
        else {
            sendQMWarning()
        }
        if (check11) {
            checkGroup10()
        }
    }

    // "2.6 Did you do anything else besides being on the phone"
    fun checkGroup10() {
        var answer10 = ""
        var check10 = false

        if (sideactivity_no.isChecked) {
            answer10 = sideactivity_no.text.toString()
            saveres(answer10, 10)
            check10 = true
        } else if (sideactivity_yes.isChecked) {
            answer10 = sideactivity_yes.text.toString()
            saveres(answer10, 10)
            check10 = true
        } else {
            sendQMWarning()
        }

        if (check10) {
            checkGroupAtHome()
        }
    }

    // "2.7 Are you currently at home"
    fun checkGroupAtHome() {
        var answerAtHome = ""
        checkAtHome = false
        if (atHome_no.isChecked) {
            answerAtHome = "false"
            saveres(answerAtHome, 233)
            checkAtHome = true
        } else if (atHome_yes.isChecked) {
            answerAtHome = "true"
            saveres(answerAtHome, 233)
            checkAtHome = true
        } else {
            sendQMWarning()
        }
        if (checkAtHome) {
            // attention check1 showed
            if (cardView1.isVisible == true) {
                checkGroup0()
            }
            // attention check2 showed
            else if (cardView2.isVisible == true){
                checkGroup6()
            }
            // no attention check showed
            else {
                answer0 = "true"
                saveres_ack(answer0, 0)
                sendData()
            }
        }
    }

    fun saveres(answer1: Int, spot: Int) {
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        if (spot == 1) {
            editor.putInt("RSHCI1", answer1)
            editor.apply()
        } else if (spot == 2) {
            editor.putInt("RSHCI2", answer1)
            editor.apply()
        } else if (spot == 3) {
            editor.putInt("RSHCI3", answer1)
            editor.apply()
        } else if (spot == 4) {
            editor.putInt("RSHCI4", answer1)
            editor.apply()
        } else if (spot == 5) {
            editor.putInt("RSHCI5", answer1)
            editor.apply()
        } else if (spot == 6) {
            editor.putInt("sense_of_agency", answer1)
            editor.apply()
        } else if (spot == 7) {
            editor.putInt("satisfaction", answer1)
            editor.apply()
        } else if (spot == 8) {
            editor.putInt("goal_alignment", answer1)
            editor.apply()
        } else if (spot == 111) {
            editor.putInt("useful_in_situation", answer1)
            editor.apply()
        } else if (spot == 12) {
            editor.putInt("stress", answer1)
            editor.apply()
        } else if (spot == 230) {
            editor.putInt("kss", answer1)
            editor.apply()
        } else if (spot == 333) {
            editor.putInt("sam", answer1)
            editor.apply()
        } else {
            Toast.makeText(this, "Problem saving your Answer", Toast.LENGTH_SHORT)
        }

    }

    fun saveres(answer1: String, spot: Int) {
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        if (spot == 11) {
            editor.putString("situation", answer1)
            editor.apply()
        } else if (spot == 10) {
            editor.putString("sideActivity", answer1)
            editor.apply()
        } else if (spot == 9) {
            editor.putString("currentActivity", answer1)
            editor.apply()
        } else if (spot == 233) {
            editor.putString("atHome", answer1)
            editor.apply()
        } else {
            Toast.makeText(this, "Problem saving your Answer", Toast.LENGTH_SHORT)
        }

    }

    fun saveres_ack(answer1: String, spot: Int) {
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        if (spot == 0 || spot == 6) {
            editor.putString("ack", answer1)
            editor.apply()
        } else {
            Toast.makeText(this, "Problem saving your Answer", Toast.LENGTH_SHORT)
        }

    }

    data class InterventionAnswers(
        val appName: String,
        val situation: String,
        val sideActivity: String,
        val currAct: String,
        val rshci1: Int,
        val rshci2: Int,
        val rshci3: Int,
        val rshci4: Int,
        val rshci5: Int,
        val stress: Int,
        val sense_of_agency: Int,
        val satisfaction: Int,
        val goal_alignment: Int,
        val useful_in_situation: Int,
        val ack: String,
        val kss: Int,
        val atHome: String,
        val sam: Int,
        val AndroidID: String,
        val delayTimeInSeconds: Long,
        val pid: String,
        val timestamp: String,
        val interventionType: String,
    )

    fun sendData() {
        Log.e("QUESTIONNAIRE", " AT SENDATA : $check11 , $check6, $check0")

        //   val delayTimeInSeconds = intent.getLongExtra("delayTimeInSeconds", 0)
        // val delayTimeFormatted = intent.getLongExtra("delayTimeFormatted", 0)
        val sharedPref = getSharedPreferences("InfiniteScroll", 0)
        //    var pid = sharedPref.getString("pID", "EMPTY")
        Log.e("PROLIFIC ID ARRIVAL", pID.toString())
        var app_name = sharedPref.getString("App_Name", "EMPTY")
        var intv_name = sharedPref.getString("Intv_Name", "EMPTY")
        var t1 = sharedPref.getString("t1", "EMPTY")
        var t2 = sharedPref.getString("t2", "EMPTY")
        //var delta = sharedPref.getString("Delta", "EMPTY")
        var rshci1 = sharedPref.getInt("RSHCI1", 404)
        var rshci2 = sharedPref.getInt("RSHCI2", 404)
        var rshci3 = sharedPref.getInt("RSHCI3", 404)
        var rshci4 = sharedPref.getInt("RSHCI4", 404)
        var rshci5 = sharedPref.getInt("RSHCI5", 404)
        var stress = sharedPref.getInt("stress", 404)
        var sense_of_agency = sharedPref.getInt("sense_of_agency", 404)
        var satisfaction = sharedPref.getInt("satisfaction", 404)
        var goal_alignment = sharedPref.getInt("goal_alignment", 404)
        var useful_in_situation = sharedPref.getInt("useful_in_situation", 404)
        var ack = sharedPref.getString("ack", "")
        var kss = sharedPref.getInt("kss", 404)
        var atHome = sharedPref.getString("atHome", "")
        var sam = sharedPref.getInt("sam", 404)

        var situation = sharedPref.getString("situation", "Default")
//        var location = sharedPref.getString("location", "Default")
        var sideActivity = sharedPref.getString("sideActivity", "Default") ?: "Default"
        var currAct = sharedPref.getString("currentActivity", "Default")
        //   var delayTimeInSec = sharedPref.getLong("delayTimeinSeconds", delayTimeInSeconds)

        val dID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        timestamp = sharedPref.getString("timestamp", "").toString()

        interventionType = sharedPref.getString("interventionType", "").toString()

        Log.e("Shared Pref", sharedPref.all.toString())

        val db = FirebaseFirestore.getInstance()
        Log.e("SharedPref", sharedPref.toString())
        appName = sharedPref.getString("appName", "").toString()
        pID = sharedPref.getString("pID", "").toString()

        var delayTimeInSeconds = sharedPref.getLong("delayTime", 404L)

        val interventionAnswers = rhsci1_activity.InterventionAnswers(
            appName,
            situation.toString(),
            sideActivity.toString(),
            currAct.toString(),
            rshci1,
            rshci2,
            rshci3,
            rshci4,
            rshci5,
            stress,
            sense_of_agency,
            satisfaction,
            goal_alignment,
            useful_in_situation,
            ack.toString(),
            kss,
            atHome.toString(),
            sam,
            dID,
            delayTimeInSeconds,
            pID,
            timestamp,
            interventionType
        )
        db.collection("intervention_answers")
            .add(interventionAnswers)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "Firestore",
                    "DocumentSnapshot added with ID: ${documentReference.id}"
                )
            }
            .addOnFailureListener { e -> Log.w("Firestore", "Error adding document", e) }
        gon()
    }


    fun postJsonToServer(
        url: String,
        jsonObject: JSONObject,
        credentials: String,
        callback: (response: String?) -> Unit,
    ) {
        val client = OkHttpClient()
        //val intent = Intent(this, WelcomeActivity::class.java)
        var intent = Intent(this, AppCheckerService::class.java)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .addHeader("Authorization", credentials)
            .addHeader("Content-Type", "text/csv")
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                callback(responseBody)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }
        })
    }


}