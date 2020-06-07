package com.example.Doctor_Appointment

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class database_helper(context:Context):SQLiteOpenHelper(context, databasename,factory, version) {
    companion object {
        internal val databasename = "userdatabase"
        internal val factory = null
        internal val version = 20
    }

    override fun onCreate(p: SQLiteDatabase?) {
        p?.execSQL(
            "create table newuser(id integer primary key autoincrement," + "name varchar(50) unique,email varchar(150) unique,password " +
                    "varchar(30))"
        )
        p?.execSQL(
            "create table doctor(id integer primary key ,doctor_name varchar(30),specialist varchar(30)," + "hospital_name " +
                    "varchar(50)," +
                    "available_time varchar(30))"
        )
        p?.execSQL(
            "insert into doctor values(1,'Pruthvi','Neurologist','Ramiah','9-12'),(2,'A','Neurologist','Apollo','3-4')," +
                    "(3,'Vishwas','Dentist','Ramiah','9-12'),(4,'Vishwas','Dentist','Kc General','3-5'), " +
                    "(17,'Bhargav','Neurologist','Victoria','2-4'),(5,'Bhargav','Neurologist','Kc General','10-12'),(6,'Rachana','Dentist','Victoria','9-12'),"+
                    "(7,'Rachana','Dentist','Apollo','2-4'),(8,'Ajay','Dentist','Columbia Asia','10-12'),(9,'Ajay','Dentist','Apollo','2-4'),"+
                    "(10,'Muzzamil','General','Apollo','9-4'),(11,'Darshan','Cardiologist','Ramiah','10-12'),"+
                    "(18,'Rajesh','Dermatologist','Ramiah','10-1'),(12,'Sujay','ENT','Apollo','1-3'),"+
                    "(13,'Laxman','Pediatrician','Victoria','1-4'),(14,'Vishak','Gynaecologist','Victoria','1-3'),"+
                    " (19,'Vishak','Gynaecologist','Apollo','10-11'),(15,'Harsha','General','Victoria','9-4'),"+
                    "(16,'Tharun','General','Columbia Asia','1-4')"
        )
        p?.execSQL(
            "create table appointment(appointment_id integer primary key autoincrement,mail_id varchar (150),doctor_name varchar(50)," +
                    "hospital_name varchar(50),specialist varchar(30),appointment_date date,appointment_timing varchar(30))"
        )


    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists newuser")
        db?.execSQL("drop table if exists doctor")
        db?.execSQL("drop table if exists appointment")
        onCreate(db)
        //To change body of created functions use File | Settings | File Templates.
    }

    fun insertdata(name: String, email: String, password: String):String {
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        val query2="select *from newuser where name='$name' and email='$email'"
        val e=db.rawQuery(query2,null)
        val query="select *from newuser where name='$name'"
        val c=db.rawQuery(query,null)
        val query1="select *from newuser where email='$email'"
        val d=db.rawQuery(query1,null)
        var email_pattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        if(!(email.matches(email_pattern)))
            return "please enter a valid email_id"
        else if(c.count>0)
            return "username already exists"

        else if(d.count>0)
            return "email already exists"
        else if(e.count>0)
            return "username and email already exists"
        else {
            values.put("name", name)
            values.put("email", email)
            values.put("password", password)
            db.insert("newuser", null, values)
            db.close()
            return "registered"
        }


    }

    fun insertlogindata(email: String, password: String): Boolean {
        val db: SQLiteDatabase = writableDatabase
        val query = "select *from newuser where email='$email' and password='$password'"
        val c = db.rawQuery(query, null)
        if (c.count <= 0) {
            c.close()
            return false
        } else {
            c.close()
            return true
        }


    }

    fun doctor_list(specialist: String): List<String> {
        val db: SQLiteDatabase = writableDatabase
        val query = "select doctor_name,hospital_name from doctor where specialist='$specialist' "
        val c = db.rawQuery(query, null)
        val doctor_list = mutableListOf<String>()
        val hospital_list = mutableListOf<String>()
        if (c.count > 0) {
            while (c.moveToNext()) {
                var d_name = c.getString(c.getColumnIndex("doctor_name"))
                var h_name = c.getString(c.getColumnIndex(("hospital_name")))
                doctor_list.add(d_name +" "+ h_name)


            }
        }
        return doctor_list

    }

    fun timings(
        selected_doctor: String,
        selected_type: String,
        selected_date: String,
        selected_hospital: String
    ): List<String> {
        val db: SQLiteDatabase = writableDatabase
        val query =
            "select appointment_timing from appointment where doctor_name='$selected_doctor' and specialist='$selected_type'" +
                    "and appointment_date='$selected_date' and hospital_name='$selected_hospital'"
        val query1 =
            "select available_time from doctor where doctor_name='$selected_doctor' and hospital_name='$selected_hospital'" +
                    "and specialist='$selected_type'"
        var booked_slots1 = mutableListOf<String>()
        var available_slots = mutableListOf<String>()
        val c = db.rawQuery(query, null)
        val d = db.rawQuery(query1, null)
        while (c.moveToNext()) {
            var booked_slots = c.getString(c.getColumnIndex("appointment_timing"))
            booked_slots1.add(booked_slots)
        }
        while (d.moveToNext()) {
            var timing = d.getString(d.getColumnIndex("available_time"))
            var i: Int
            var count = 0


            var (a, b) = timing.split("-")

            var c: Int = a.toInt()
            var d: String
            while (c != b.toInt()) {
                i = c
                if (count % 2 == 0) {
                    if (c <= 9) {
                        d = 0.toString() + i.toString() + ":" + "00" + ":" + "00"

                    } else {
                        d = i.toString() + ":" + "00" + ":" + "00"

                    }
                } else {
                    if (c <= 9) {
                        d = 0.toString() + i.toString() + ":" + "30" + ":" + "00"
                    } else {
                        d = i.toString() + ":" + "30" + ":" + "00"
                    }

                }
                if (!(d in booked_slots1)) {
                    available_slots.add(d)
                }
                if (count % 2 == 0) {
                    c = i


                } else {
                    c = i + 1
                }
                count = count + 1
            }


        }

        return available_slots
    }
    fun insert_appointment_data(doctor_name:String,hospital_name:String,specialist: String,appointment_Date:String,appointment_time :String,email:String):String{
        val db:SQLiteDatabase=writableDatabase
        var id=0
        val query="insert into appointment(mail_id,doctor_name,hospital_name,specialist,appointment_date,appointment_timing) values('$email','$doctor_name','$hospital_name','$specialist','$appointment_Date','$appointment_time')"
        val query1="select appointment_id from appointment where doctor_name='$doctor_name' and hospital_name='$hospital_name' and " +
                "specialist='$specialist' and appointment_date='$appointment_Date' and appointment_timing='$appointment_time'"

        db.execSQL(query)

        val c=db.rawQuery(query1,null)
        while(c.moveToNext())
        {
             id=c.getInt(c.getColumnIndex("appointment_id"))
            print(id)

        }


    return id.toString()
    }
    fun deleteappointment(appointment_id:String,mail_id:String):Boolean{
        val db:SQLiteDatabase=writableDatabase
        var a=appointment_id.toInt()
        return (db.delete("appointment","appointment_id='$appointment_id' and mail_id='$mail_id' ",null))>0

    }
}