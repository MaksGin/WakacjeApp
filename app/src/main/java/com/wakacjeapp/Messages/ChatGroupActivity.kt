package com.wakacjeapp.Messages

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.wakacjeapp.R
import com.wakacjeapp.databinding.ActivityChatGroupBinding
import com.wakacjeapp.databinding.ActivityChatLogBinding
import java.util.Locale

class ChatGroupActivity : AppCompatActivity() {

    private var binding: ActivityChatGroupBinding? = null
    private lateinit var messageAdapter: ChatMessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageBox: EditText
    private lateinit var myref: DatabaseReference
    private var senderNameMap = HashMap<String, String>()
    var groupRoom: String? = null
    var senderUid: String? = null

    // A fused location client variable which is further user to get the user's current location
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatGroupBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val groupName = intent.getStringExtra("groupName")
        senderUid = FirebaseAuth.getInstance().currentUser?.uid
        myref = FirebaseDatabase.getInstance().reference

        groupRoom = "groups/$groupName"



        // Initialize the Fused location variable
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this) //aktualna lokalizacja uzytkownika



        if (!isLocationEnabled()) { //sprawdzenie usługi lokalizacji
            Toast.makeText(
                this,
                "Your location provider is turned off. Please turn it on.",
                Toast.LENGTH_SHORT
            ).show()

            // przejscie do ustawien lokalizacji
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report!!.areAllPermissionsGranted()) {
                            requestLocationData()
                        }

                        if (report.isAnyPermissionPermanentlyDenied) {

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        TODO("Not yet implemented")
                    }


                }).onSameThread()
                .check()
        }

        supportActionBar?.title = groupName

        messageList = ArrayList()
        messageAdapter = ChatMessageAdapter(this,messageList)

        binding?.rvGroupChatLog?.layoutManager = LinearLayoutManager(this@ChatGroupActivity)
        binding?.rvGroupChatLog?.adapter = messageAdapter
        messageBox = binding?.enterGroupMsg!!

        binding?.btnSendGroup?.setOnClickListener{

            val message = messageBox.text.toString()
            val userRef = FirebaseDatabase.getInstance().reference.child("users").child(senderUid!!)
            userRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val senderName = snapshot.child("name").getValue(String::class.java)
                    val messageObject = Message(message, senderUid, senderName)

                    myref.child(groupRoom!!).child("messages").push() //update UI wysyłającego i odbiorcy
                        .setValue(messageObject).addOnSuccessListener {
                            Log.i("groupRoom", groupRoom!!)
                            Log.i("senderUid", senderUid!!)
                        }
                    messageBox.setText("")
                }

                override fun onCancelled(error: DatabaseError) {
                    // obsługa błędów
                }
            })




        }

        //logika dodawania wiadomosci do recycleView
        myref.child(groupRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        val senderId = message?.senderId
                        if (senderId != null && !senderNameMap.containsKey(senderId)) {
                            myref.child("users").child(senderId).child("name")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val senderName = snapshot.getValue(String::class.java)
                                        if (senderName != null) {
                                            senderNameMap[senderId] = senderName
                                            message.senderName = senderName
                                            messageList.add(message)
                                            messageAdapter.notifyDataSetChanged()
                                            val lastindex = binding?.rvGroupChatLog?.adapter?.itemCount?.minus(1) ?: 0
                                            binding?.rvGroupChatLog?.scrollToPosition(lastindex)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                        } else {
                            message?.senderName = senderNameMap[senderId]
                            messageList.add(message!!)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })




    }

    private fun isLocationEnabled(): Boolean {

        val locationManager: LocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationData() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }



    //obiekt klasy anonimowej, reagujacy na wyniki lokalizacji
    private val mLocationCallback = object : LocationCallback() {
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            val latitude = mLastLocation!!.latitude
            Log.i("Current Latitude", "$latitude")

            val longitude = mLastLocation!!.longitude
            Log.i("Current Longitude", "$longitude")




            val geocoder = Geocoder(this@ChatGroupActivity, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude,1) as List<Address>

            val address: String = addresses[0].getAddressLine(0)
            val city: String = addresses[0].locality
            val state: String = addresses[0].adminArea
            val country: String = addresses[0].countryName
            val postalCode: String = addresses[0].postalCode
            val knownName: String = addresses[0].featureName


            binding?.btnLocation?.setOnClickListener {
                binding?.enterGroupMsg?.setText(address)
            }
        }
    }


}
