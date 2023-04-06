package com.carelon.whe.di

import com.carelon.whe.data.*
import com.carelon.whe.data.data_source.UpdateMedicationUseCaseImpl
import com.carelon.whe.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun bindAddMedicationUseCase(useCaseImpl: AddMedicationUseCaseImpl) : AddMedicationUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindUpdateMedicineUseCase(useCaseImpl: UpdateMedicationUseCaseImpl) : UpdateMedicationUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindForYouUseCase(useCaseImpl: ForYouUseCaseImpl) : ForYouUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindMedicationUseCase(useCaseImpl: MedicationUseCaseImpl) : MedicationUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindMedicationDetailUseCase(useCaseImpl: MedicationDetailUseCaseImpl) : MedicationDetailUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindLoginUseCase(useCaseImpl: LoginUseCaseImpl) : LoginUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindConsentUseCase(useCaseImpl: ConsentUseCaseImpl) : ConsentUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindAppFeatureUseCase(useCaseImpl: AppFeaturesUseCaseImpl) : AppFeaturesUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindEmailVerificationUseCase(useCaseImpl: EmailVerificationUseCaseImpl) : EmailVerificationUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindUserProfileUseCase(useCaseImpl: UserProfileUseCaseImpl) : UserProfileUseCase
}