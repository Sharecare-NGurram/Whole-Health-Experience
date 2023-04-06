package com.carelon.whe.ui.dashboard.meds

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseFragment
import com.carelon.whe.base.BaseViewHolder
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.databinding.FragmentMedsBinding
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.ui.add_medication.AddMedicationActivity
import com.carelon.whe.ui.dashboard.DashboardActivity
import com.carelon.whe.ui.dashboard.meds.adapter.MedicineRemindersAdapter
import com.carelon.whe.ui.dashboard.meds.adapter.NewAddedMedicationAdapter
import com.carelon.whe.ui.dashboard.meds.adapter.SavedMedicationAdapter
import com.carelon.whe.ui.medication_details.MedicationDetailsFragment
import com.carelon.whe.ui.medication_details.OnMedicationDeleteListener
import com.carelon.whe.ui.medicine_reminder.MedicineReminderActivity
import com.carelon.whe.util.*
import com.google.android.flexbox.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MedsFragment : BaseFragment<FragmentMedsBinding>() {

    private val medsViewModel by viewModels<MedsViewModel>()

    @Inject
    lateinit var medicineRemindersAdapter: MedicineRemindersAdapter

    @Inject
    lateinit var mSavedMedsAdapter: SavedMedicationAdapter

    @Inject
    lateinit var mNewPrescriptionAdapter: NewAddedMedicationAdapter

    override fun getContentView(): Int = R.layout.fragment_meds

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): BaseViewModel = medsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initView()
    }

    private fun initView() {
        setSavedMedicationAdapterToUI()
        setNewPrescriptionAdapterToUI()
        setMedicineReminderAdapterToUI()
    }

    /**
     * set saved medicine user has added to his account
     * */
    private fun setSavedMedicationAdapterToUI() {
        safeLet(getDataBinding(), context) { _binding, _context ->
            _binding.recyclerSavedMedications.adapter = mSavedMedsAdapter
            /**
             * Listener for list item click, it will take to add/edit medication details
             */
            mSavedMedsAdapter.clickListenerWithPosition =
                object : BaseViewHolder.ItemClickedCallBackWithPosition<Medicine?> {
                    override fun onItemClicked(data: Medicine?, pos: Int) {
                        Logger.logD(message = data?.name ?: "")
                        data?.let { medicine ->
                            val fragment = MedicationDetailsFragment()
                            val dataToPass = Bundle()
                            dataToPass.putParcelable(
                                BundleKeyConstants.MEDICINES_TO_DISPLAY_IN_DETAILS, medicine
                            )
                            fragment.arguments = dataToPass
                            fragment.setMedicationDeleteListener(object :
                                OnMedicationDeleteListener {
                                override fun onMedicationDeleted(medicine: Medicine?) {
                                    mSavedMedsAdapter.removeDataWithPos(pos)
                                    medsViewModel.saveSelectedMeds(mSavedMedsAdapter.list)
                                    mActivity?.showSuccessToast(
                                        String.format(
                                            getString(R.string.str_meds_was_removed), medicine?.getMedicineNameForToast()
                                        )
                                    )
                                }
                            })
                            fragment.safeShow(childFragmentManager, "MedicationDetailsFragment")
                        }
                    }
                }

            _binding.textAddMeds.setSafeOnClickListener {
                //callback to add medication page
                addMedicineForResult.launch(
                    Intent(activity, AddMedicationActivity::class.java).putExtra(
                        BundleKeyConstants.EXTRA_ADD_MEDICATION_TRIGGERED_FROM,
                        Gson().toJson(mSavedMedsAdapter.list)
                    )
                )
            }

            _binding.constHelp.setSafeOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    callHelpline()
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                }
            }
        }
    }

    /**
     * set new prescription available to user
     * */
    private fun setNewPrescriptionAdapterToUI() {
        safeLet(getDataBinding(), context) { _binding, _context ->
            _binding.recyclerNewMeds.adapter = mNewPrescriptionAdapter
            /**
             * Listener for list item click, it will take to add/edit medication details
             */
            mNewPrescriptionAdapter.clickListenerWithPosition =
                object : BaseViewHolder.ItemClickedCallBackWithPosition<Medicine?> {
                    override fun onItemClicked(data: Medicine?, pos: Int) {
                        Logger.logD(message = data?.name ?: "")
                        data?.let { medicine ->
                            data.isSelected = !data.isSelected
                            mNewPrescriptionAdapter.updateDataWithPos(data, pos)
                            medsViewModel.updateSelectedItems(data)
                        }
                    }
                }

            _binding.btnSelectedMeds.setSafeOnClickListener {
                if (medsViewModel.selectedNewMedicines.size > 0) {
                    _binding.constraintNewMeds.gone()
                    medsViewModel.saveNewPrescriptionMeds(
                        medsViewModel.selectedNewMedicines, mSavedMedsAdapter.list
                    )
                    (activity as DashboardActivity).removeNotificationBatch(R.id.fragment_meds)
                    medsViewModel.markedNewPrescriptionRead()
                }
            }
            _binding.btnDontAdd.setSafeOnClickListener {
                _binding.constraintNewMeds.gone()
                (activity as DashboardActivity).removeNotificationBatch(R.id.fragment_meds)
                medsViewModel.markedNewPrescriptionRead()
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            callHelpline()
        } else {
            mActivity?.showPermissionDeniedAlert(
                getString(R.string.title_call_permission_denied),
                getString(R.string.message_call_permission_denied),
                getString(R.string.settings)
            )
        }
    }

    private fun callHelpline() {
        // TODO: Phone number need to be updated
        try {
            val phone = "+10000000000"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * set flex layout manager for showing flexible grid
     * */
    private fun setMedicineReminderAdapterToUI() {
        safeLet(getDataBinding(), context) { _binding, _context ->
            _binding.recyclerSavedReminder.layoutManager = FlexboxLayoutManager(_context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START
            }
            _binding.recyclerSavedReminder.adapter = medicineRemindersAdapter
            /**
             * Listener for list item click, it will take to add/edit medication reminder time
             */
            medicineRemindersAdapter.listenerCallback =
                object : MedicineRemindersAdapter.ItemAddClickedCallBack<MedicineReminder> {
                    /**
                     * callback to edit existing reminder
                     */
                    override fun onItemClicked(data: MedicineReminder, pos: Int) {
                        val showExistingMedicineReminderList =
                            medicineRemindersAdapter.listSize >= 1
                        val medicineReminderIntent =
                            Intent(requireActivity(), MedicineReminderActivity::class.java).apply {
                                putExtra(BundleKeyConstants.EXTRA_MEDICINE_REMINDER_ITEM, data)
                                putExtra(
                                    BundleKeyConstants.EXTRA_SHOW_EXISTING_REMINDER_LIST,
                                    showExistingMedicineReminderList
                                )
                                putExtra(BundleKeyConstants.EXTRA_EDIT_MEDICINE_REMINDER, true)
                                putParcelableArrayListExtra(
                                    BundleKeyConstants.EXTRA_MEDICINE_REMINDERS_LIST,
                                    ArrayList(medicineRemindersAdapter.list)
                                )
                            }
                        startForResult.launch(medicineReminderIntent)
                    }

                    /**
                     * callback to add a new reminder
                     */
                    override fun onAddNewItem() {
                        val showExistingMedicineReminderList =
                            medicineRemindersAdapter.listSize >= 1
                        val medicineReminderIntent =
                            Intent(requireActivity(), MedicineReminderActivity::class.java).apply {
                                putExtra(BundleKeyConstants.EXTRA_EDIT_MEDICINE_REMINDER, false)
                                putExtra(
                                    BundleKeyConstants.EXTRA_SHOW_EXISTING_REMINDER_LIST,
                                    showExistingMedicineReminderList
                                )
                                putParcelableArrayListExtra(
                                    BundleKeyConstants.EXTRA_MEDICINE_REMINDERS_LIST,
                                    ArrayList(medicineRemindersAdapter.list)
                                )
                            }
                        startForResult.launch(medicineReminderIntent)
                    }

                }
        }
        medsViewModel.getMedicineReminder()
    }

    override fun onResume() {
        super.onResume()

        fetchRecord()
    }

    /**
     * initiate data fetch from repository
     * */
    private fun fetchRecord() {
        medsViewModel.getSavedMedication()
        medsViewModel.getNewPrescriptionMedicine()
    }

    /**
     * receive data from repository
     * */
    private fun observeData() {
        medsViewModel.savedMedsLD.observe(viewLifecycleOwner) {
            mSavedMedsAdapter.addAll(it as List<Medicine>)
            mSavedMedsAdapter.notifyChange()
            if (it.isNotEmpty()) {
                getDataBinding()?.apply {
                    textYourMeds.text = getString(R.string.str_your_meds)
                    textNewPrescription.show()
                    textNoMedsDescription.gone()
                }
            } else {
                getDataBinding()?.apply {
                    textYourMeds.text = getString(R.string.str_you_have_no_meds)
                    textNewPrescription.gone()
                    textNoMedsDescription.show()
                }
            }
        }
        medsViewModel.medsReminderLD.observe(viewLifecycleOwner) { _medicineReminderList ->
            medicineRemindersAdapter.addAll(_medicineReminderList)
            medicineRemindersAdapter.notifyChange()
        }
        medsViewModel.newPrescriptionLD.observe(viewLifecycleOwner) { _newPrescriptionMeds ->
            if (!_newPrescriptionMeds.isNullOrEmpty()) {
                getDataBinding()?.constraintNewMeds?.show()
                mNewPrescriptionAdapter.addAll(_newPrescriptionMeds)
                mNewPrescriptionAdapter.notifyChange()
            } else {
                getDataBinding()?.constraintNewMeds?.gone()
            }
        }
        medsViewModel.selectedNewMedicinesLD.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                getDataBinding()?.btnSelectedMeds?.background?.setTint(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
            } else {
                getDataBinding()?.btnSelectedMeds?.background?.setTint(
                    ContextCompat.getColor(
                        requireContext(), R.color.button_disabled_white
                    )
                )
            }
        }
        medsViewModel.savedNewPrescriptionLD.observe(viewLifecycleOwner) {
            if (it > 1) {
                mActivity?.showSuccessToast(
                    String.format(
                        getString(R.string.str_added_multiple_new_prescription), it
                    )
                )
            } else {
                mActivity?.showSuccessToast(
                    String.format(
                        getString(R.string.str_added_new_prescription), it
                    )
                )
            }
        }
    }

    /**
     * go to meds section to edit new meds
     **/
    private fun goToEditMedication(data: Medicine? = null) {

    }

    //Callback to show the medicine reminder added snack bar
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val medicineReminderTimeSelected =
                    intent?.getStringExtra(BundleKeyConstants.EXTRA_MEDICINE_REMINDER_TIME_SELECTED)
                        ?: ""
                if (medicineReminderTimeSelected.isNotEmpty()) {
                    mActivity?.showSuccessToast(medicineReminderTimeSelected)
                }
                medsViewModel.getMedicineReminder()
            }
        }

    //Callback to get new added medicine and update it
    private val addMedicineForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val medicineCount =
                    intent?.getIntExtra(BundleKeyConstants.EXTRA_IMPORT_MEDICINES_COUNT, 0)
                val selectedMeds = intent?.getStringExtra(BundleKeyConstants.EXTRA_IMPORT_MEDICINES)
                val type = object : TypeToken<List<MedicationData>>() {}.type
                val medicationsList = Gson().fromJson(selectedMeds, type) as ArrayList<MedicationData>
                if(medicationsList.isNotEmpty()) {
                    val list = medicationsList.map{ dt-> Medicine.ModelMapper.from(dt)} as ArrayList<Medicine>
                    list.addAll(mSavedMedsAdapter.list)
                    medsViewModel.saveSelectedMeds(list)

                    if (medicineCount == 1) {
                        mActivity?.showSuccessToast(
                            String.format(
                                getString(R.string.str_added_medication),
                                medicationsList.first().getMedicineNameForToast()
                            )
                        )
                    } else {
                        mActivity?.showSuccessToast(
                            String.format(
                                getString(R.string.str_added_more_than_one_medication),
                                medicineCount
                            )
                        )
                    }
                }
                medsViewModel.markedImportMedsCompleted()
            }
        }
}