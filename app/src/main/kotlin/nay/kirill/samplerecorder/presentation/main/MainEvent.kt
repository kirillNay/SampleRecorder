package nay.kirill.samplerecorder.presentation.main

sealed interface MainEvent {

    data class ShareFile(val fileDirectory: String) : MainEvent

    data class FailureToast(val message: String?) : MainEvent

}