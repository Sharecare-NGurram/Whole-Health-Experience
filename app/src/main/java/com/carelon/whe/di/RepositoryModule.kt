package com.carelon.whe.di


import com.carelon.whe.data.*
import com.carelon.whe.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAddMedicationRepository(addMedicationRepository: AddMedicationRepositoryImpl): AddMedicationRepository

    @Binds
    abstract fun bindUpdateMedicationRepository(addMedicationRepository: UpdateMedicationRepositoryImpl): UpdateMedicationRepository

    @Binds
    abstract fun bindForYouRepository(forYouRepository: ForYouRepositoryImpl): ForYouRepository

    @Binds
    abstract fun bindMedicationRepository(medicationRepositoryImpl: MedicationRepositoryImpl): MedicationRepository

    @Binds
    abstract fun bindMedicineReminderRepository(medicineReminderRepositoryImpl: MedicineReminderRepositoryImpl): MedicineReminderRepository

    @Binds
    abstract fun bindMedicineDetailRepository(medicationDetailRepositoryImpl: MedicationDetailRepositoryImpl): MedicationDetailRepository

    @Binds
    abstract fun bindLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindConsentRepository(repositoryImpl: ConsentRepositoryImpl): ConsentRepository

    @Binds
    abstract fun bindAppFeatureRepository(AppFeatureRepositoryImpl: AppFeaturesRepositoryImpl): AppFeaturesRepository

    @Binds
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun bindFocusAreasRepository(focusAreasRepositoryImpl: FocusAreasRepositoryImpl): FocusAreasRepository

    @Binds
    abstract fun bindEmailVerificationRepository(emailVerificationRepositoryImpl: EmailVerificationRepositoryImpl): EmailVerificationRepository

    @Binds
    abstract fun bindTrackYourStepsRepository(trackYourStepsRepository: TrackYourStepsRepositoryImpl): TrackYourStepsRepository

    @Binds
    abstract fun bindGoogleFitRepository(googleFitRepositoryImpl: GoogleFitRepositoryImpl): GoogleFitRepository
}