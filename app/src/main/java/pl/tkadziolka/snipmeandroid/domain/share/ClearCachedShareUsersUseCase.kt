package pl.tkadziolka.snipmeandroid.domain.share

import pl.tkadziolka.snipmeandroid.domain.repository.share.ShareRepository

class ClearCachedShareUsersUseCase(private val shareRepository: ShareRepository) {
    operator fun invoke() {
        shareRepository.clearCachedUsers()
    }
}