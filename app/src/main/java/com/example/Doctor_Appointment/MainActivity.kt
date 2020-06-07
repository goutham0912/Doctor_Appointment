package com.example.Doctor_Appointment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appointment_date.*
import kotlinx.android.synthetic.main.book_appointment1.*

import kotlinx.android.synthetic.main.book_appointment4.*
import kotlinx.android.synthetic.main.delete_appointment.*
import kotlinx.android.synthetic.main.home_page.*

import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.select_doctor.*
import kotlinx.android.synthetic.main.select_timing.*
import kotlinx.android.synthetic.main.user_registration.*

class MainActivity : AppCompatActivity() {
    lateinit var handler: database_helper
    internal lateinit var sp:Spinner
    internal lateinit var sp1:Spinner
    internal  lateinit var sp2:Spinner
    internal lateinit var sp3:Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler=database_helper(this)
        sp=findViewById(R.id.dropdown) as Spinner
        val dd=arrayOf("Neurologist","Dentist","Pediatrician","Cardiologist","ENT","Gynaecologist")
        val adapter1=ArrayAdapter(this,android.R.layout.simple_list_item_1,dd)
        sp.adapter=adapter1

        homepage()
        registration.setOnClickListener{
            showRegisterationform()
        }
        login.setOnClickListener{
            showLoginform()
        }
        save.setOnClickListener{
           var a= handler.insertdata(name.text.toString(),email.text.toString(),passwordregister.text.toString())
            if(a=="registered")
                homepage()
            else {
                Toast.makeText(this, a, Toast.LENGTH_SHORT).show()

            }

        }
        loginuser.setOnClickListener {

            if(handler.insertlogindata(login_email.text.toString(),login_password.text.toString())) {
                homepage1()
                Toast.makeText(this, "login Successful", Toast.LENGTH_SHORT).show()
                book_appointment.setOnClickListener {
                    book_appointment1()
                    val date = findViewById<DatePicker>(R.id.appointment_Date)
                    submit1.setOnClickListener {
                        var date = findViewById<DatePicker>(R.id.appointment_Date)
                        select_Date()
                        submit_date.setOnClickListener {


                        var selected_specialist = dropdown.selectedItem.toString()
                        var doctor_list1 = handler.doctor_list(selected_specialist)
                        var layout = findViewById<LinearLayout>(R.id.select_doctor)

                        var btn2 = Button(this)
                        btn2.text = "submit"
                        var l = (doctor_list1.size) - 1
                        var doctor = findViewById<RadioGroup>(R.id.r_doctor)
                        for (i in 0..l) {
                            var layout = findViewById<LinearLayout>(R.id.select_doctor)

                            var btn1 = RadioButton(this)
                            btn1.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            var (text1,text2) = doctor_list1[i].split(" ")
                            btn1.text = "$text1" +"\n"+"$text2"+" "+"hospital"
                            doctor.addView(btn1)


                        }
                        layout.addView(btn2)
                        btn2.text="select time"
                        select_doctor()
                        btn2.setOnClickListener {
                            var rid = doctor.checkedRadioButtonId
                            var doctor_id = findViewById<RadioButton>(rid)

                            var selected_doctor = doctor_id.text.toString()
                            var (a, b) = selected_doctor.split("\n")
                            var(h1,h2)=b.split(" ")
                            var c = date.year.toString()
                            var d = date.month.toString()
                            var e = date.dayOfMonth.toString()
                            var f = e + "/" + d + "/" + c

                            var available_slots = handler.timings(
                                a.toString(),
                                dropdown.selectedItem.toString(),
                                f.toString(),
                                h1.toString()
                            )
                            var length = (available_slots.size) - 1
                            var layout1 = findViewById<LinearLayout>(R.id.select_timing)
                            var btn3 = Button(this)
                            for (i in 0..length) {
                                var layout1 = findViewById<LinearLayout>(R.id.select_timing)
                                var r = findViewById<RadioGroup>(R.id.r_timing)
                                var rtn = RadioButton(this)
                                rtn.layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                var text = available_slots[i]
                                rtn.text = "$text"
                                rtn.id = i
                                r_timing.addView(rtn)

                                select_timing()
                            }
                            select_timing()
                            layout1.addView(btn3)
                            btn3.text="Submit"
                            btn3.setOnClickListener {
                                var radioid = r_timing.checkedRadioButtonId
                                var r = findViewById<RadioButton>(radioid)

                                var selected_time = r.text.toString()
                                var id1 = handler.insert_appointment_data(
                                    a.toString(),
                                    h1.toString(),
                                    selected_specialist,
                                    f.toString(),
                                    selected_time.toString(),
                                    login_email.text.toString()
                                )
                                book_appointment4()
                                appointment_id.text = id1.toString()
                                doctor_name1.text = a.toString()
                                hospital_name.text = h1.toString()
                                appointment_Date1.text = f.toString()
                                timing.text = selected_time.toString()
                                submit4.setOnClickListener {
                                    homepage1()


                                }
                            }

//


                        }
                    }


                        }


                    }

                delete_appointment2.setOnClickListener {
                    deleteappointment()
                    delete.setOnClickListener {
                        var delete = handler.deleteappointment(appointment_id1.text.toString(),login_email.text.toString())
                        if (delete) {
                            Toast.makeText(this, "appointment deleted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "enter a valid appointment id", Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                    homepage.setOnClickListener {
                        var l=
                        homepage1()

                    }


                }

            }
            else
            {
                Toast.makeText(this,"username or password is incorrect ",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showRegisterationform()
    {
    registraion_layout.visibility=View.VISIBLE
        login_layout.visibility=View.GONE
        home.visibility=View.GONE

        book_appointment4.visibility=View.GONE
        home_page.visibility=View.GONE
        delete_appointment.visibility=View.GONE
        select_timing.visibility=View.GONE
        select_doctor.visibility=View.GONE
        appointment_date.visibility=View.GONE


    }
    private  fun showLoginform()
    {
        registraion_layout.visibility=View.GONE
        login_layout.visibility=View.VISIBLE
        home.visibility=View.GONE

        book_appointment4.visibility=View.GONE
        home_page.visibility=View.GONE
        delete_appointment.visibility=View.GONE
        select_timing.visibility=View.GONE
        select_doctor.visibility=View.GONE
        appointment_date.visibility=View.GONE
    }
    private  fun homepage()
    {
        registraion_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        home.visibility=View.VISIBLE

        book_appointment4.visibility=View.GONE
        home_page.visibility=View.GONE
        delete_appointment.visibility=View.GONE
        select_timing.visibility=View.GONE
        select_doctor.visibility=View.GONE
        appointment_date.visibility=View.GONE

    }
    private fun book_appointment1()
    {
        registraion_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        home.visibility=View.GONE
        book_appointment1.visibility=View.VISIBLE

        book_appointment4.visibility=View.GONE
        home_page.visibility=View.GONE
        delete_appointment.visibility=View.GONE
        select_timing.visibility=View.GONE
        select_doctor.visibility=View.GONE
        appointment_date.visibility=View.GONE

    }

    private fun book_appointment4()
    {
        registraion_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        home.visibility=View.GONE
        book_appointment1.visibility=View.GONE

        book_appointment4.visibility=View.VISIBLE
        home_page.visibility=View.GONE
        delete_appointment.visibility=View.GONE
        select_timing.visibility=View.GONE
        select_doctor.visibility=View.GONE
        appointment_date.visibility=View.GONE

    }
    private fun homepage1(){
        registraion_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        home.visibility=View.GONE
        book_appointment1.visibility=View.GONE

        book_appointment4.visibility=View.GONE
        home_page.visibility=View.VISIBLE
        delete_appointment.visibility=View.GONE
        select_timing.visibility=View.GONE
        select_doctor.visibility=View.GONE
        appointment_date.visibility=View.GONE

    }
    private fun deleteappointment()
    {
        registraion_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        home.visibility=View.GONE
        book_appointment1.visibility=View.GONE

        book_appointment4.visibility=View.GONE
        home_page.visibility=View.GONE
        delete_appointment.visibility=View.VISIBLE
        select_timing.visibility=View.GONE
        select_doctor.visibility=View.GONE
        appointment_date.visibility=View.GONE


    }
    private fun select_timing()
    {
        registraion_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        home.visibility=View.GONE
        book_appointment1.visibility=View.GONE

        book_appointment4.visibility=View.GONE
        home_page.visibility=View.GONE
        delete_appointment.visibility=View.GONE
        select_timing.visibility=View.VISIBLE
        select_doctor.visibility=View.GONE
        appointment_date.visibility=View.GONE
    }
    private fun select_doctor()
    {
        registraion_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        home.visibility=View.GONE
        book_appointment1.visibility=View.GONE

        book_appointment4.visibility=View.GONE
        home_page.visibility=View.GONE
        delete_appointment.visibility=View.GONE
        select_timing.visibility=View.GONE
        select_doctor.visibility=View.VISIBLE
        appointment_date.visibility=View.GONE
    }
    private  fun select_Date()
    {
        registraion_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        home.visibility=View.GONE
        book_appointment1.visibility=View.GONE

        book_appointment4.visibility=View.GONE
        home_page.visibility=View.GONE
        delete_appointment.visibility=View.GONE
        select_timing.visibility=View.GONE
        select_doctor.visibility=View.GONE
        appointment_date.visibility=View.VISIBLE
    }
}
