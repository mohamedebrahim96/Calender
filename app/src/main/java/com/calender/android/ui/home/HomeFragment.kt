package com.calender.android.ui.home

import android.Manifest
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.calender.android.R
import com.calender.android.databinding.FragmentHomeBinding
import com.calender.android.ui.base.BaseFragment
import com.calender.android.utils.Constants
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.common.AccountPicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


/**
 * Created by @mohamedebrahim96 on 16,May,2022
 * ShopiniWorld, Inc
 * ebrahimm131@gmail.com
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home), View.OnClickListener {

    val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var accountName: String
    private lateinit var accountType: String
    private lateinit var prefs: SharedPreferences
    private val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,                     // 0
        CalendarContract.Calendars.ACCOUNT_NAME,            // 1
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 2
        CalendarContract.Calendars.OWNER_ACCOUNT            // 3
    )
    private var calId: Long? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun initViews() {
        prefs = requireContext().getSharedPreferences("CalendarAPI", Context.MODE_PRIVATE)
        binding.deleteCalenderFAB.setOnClickListener(this)
        binding.createEventsBTN.setOnClickListener(this)
        binding.accountPicker.setOnClickListener(this)
        binding.allowPermissions.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.allowPermissions -> allowPermissions()
            R.id.deleteCalenderFAB -> deleteEvents()
            R.id.createEventsBTN -> createEvents()
            R.id.accountPicker -> {
                val googlePicker = AccountPicker.newChooseAccountIntent(
                    null, null, arrayOf(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE),
                    true, null, null, null, null
                )
                startActivityForResult(googlePicker, Constants.REQUEST_CODE)


                val accountManager =
                    requireActivity().getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

//                val accountManager =
//                    requireActivity().getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
                val accounts = accountManager.accounts
                println("PrintingAccounts: " + accounts.size)
                for (a in accounts) {
                    System.err.println("AccountName: " + a.name)
                }


                val accounts2 = accountManager.getAccountsByType(null)
                for (account in accounts2) {
                    Log.d("TAG", "account: " + account.name + " : " + account.type)
                }

                val accounts3 = AccountManager.get(requireContext()).getAccountsByType("com.google")

                for (account in accounts3) {
                    Log.i("accounts3", "${account.name} : ${account.type}")
                }
            }
        }
    }


    private fun createEvents() {
        getCalenderId()
        val startMillis: Long = Calendar.getInstance().run {
            timeInMillis + (60000 * 2) // Start time two minutes after
        }
        val endMillis: Long = Calendar.getInstance().run {
            timeInMillis + (60000 * 10)// End time ten minutes after
        }
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, "Fever with headache")
            put(CalendarContract.Events.DESCRIPTION, "Patient Visit")
            put(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=7")
            put(CalendarContract.Events.CALENDAR_ID, prefs.getLong("CALID", -1))
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().displayName)
        }
        val uri: Uri =
            requireContext().contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)!!
        val eventID: Long = uri.lastPathSegment!!.toLong()
        prefs.edit().putLong("EventId", eventID).apply()
        Toast.makeText(requireContext(), "Event Created", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Range")
    private fun getCalenderId() {
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        val selection: String = "((${CalendarContract.Calendars.ACCOUNT_NAME} = ?) AND (" +
                "${CalendarContract.Calendars.ACCOUNT_TYPE} = ?) AND (" +
                "${CalendarContract.Calendars.OWNER_ACCOUNT} = ?))"
        val selectionArgs: Array<String> =
            arrayOf(accountName, accountType, accountName)
        val cur: Cursor =
            requireActivity().contentResolver.query(
                uri, EVENT_PROJECTION, selection, selectionArgs, null
            )!!
        cur.use { curs ->
            // Use the cursor to step through the returned records
            while (cur.moveToNext()) {

                // Get the field values
                calId = curs.getLong(curs.getColumnIndex(CalendarContract.Calendars._ID))
                prefs.edit().putLong("CALID", calId!!).apply()

                // Selected Account Name
                val accountName: String =
                    curs.getString(curs.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME))

                // Selected Display Name
                val displayName: String =
                    curs.getString(
                        curs.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
                    )

                // Selected Account Owner
                val ownerName: String =
                    curs.getString(
                        curs.getColumnIndex(CalendarContract.Calendars.OWNER_ACCOUNT)
                    )
            }
        }
    }

    private fun deleteEvents() {
        val eventId = prefs.getLong("EventId", -1)
        val deleteUri: Uri =
            ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        val rows: Int = requireActivity().contentResolver.delete(deleteUri, null, null)
        Log.i("TEST", "Rows deleted: $rows")
        Toast.makeText(requireContext(), "Event Deleted.", Toast.LENGTH_SHORT).show()
    }


    private fun allowPermissions() {
        checkPermissions(42, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
    }


    private fun checkPermissions(callbackId: Int, vararg permissionsId: String) {
        var permissions = true
        for (p in permissionsId) {
            permissions =
                permissions && ContextCompat.checkSelfPermission(
                    requireContext(),
                    p
                ) == PERMISSION_GRANTED
        }
        if (!permissions) ActivityCompat.requestPermissions(
            requireActivity(),
            permissionsId,
            callbackId
        )
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        // Return selected account details like account name, accountType and so on.
        if (requestCode == Constants.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            accountName = data!!.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)!!
            accountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)!!
            Toast.makeText(requireContext(), "Account Selected.", Toast.LENGTH_SHORT).show()
        }
    }

}