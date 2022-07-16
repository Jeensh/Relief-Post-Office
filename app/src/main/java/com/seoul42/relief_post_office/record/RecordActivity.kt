package com.seoul42.relief_post_office.record

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
<<<<<<< HEAD
=======
import android.util.Log
>>>>>>> origin/seunjang
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.seoul42.relief_post_office.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class RecordActivity(view: View) {

    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    private val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

    private val dateAndTime : LocalDateTime by lazy {
        LocalDateTime.now()
    }

    private val recordTimeTextView: RecordCountTime by lazy {
        view.findViewById(R.id.question_setting_time)
    }

<<<<<<< HEAD
    private val recordDurationTextView: EditRecordDurationTime by lazy {
        view.findViewById(R.id.question_duration_time)
    }

=======
>>>>>>> origin/seunjang
    private val resetButton: Button by lazy {
        view.findViewById(R.id.question_record_settingBtn)
    }

    private val recordButton: RecordButton by lazy {
        view.findViewById(R.id.question_setting_playBtn)
    }

    // 요청할 권한들을 담을 배열에 음성 녹음 관련 권한을 담아줍니다.
    private val recordingFilePath: String by lazy {
<<<<<<< HEAD
        "${view.context.externalCacheDir?.absolutePath}/${auth.currentUser?.uid + dateAndTime.format(formatter)}.3gp"
=======
        "${view.context.externalCacheDir?.absolutePath}/recording.3gp"
>>>>>>> origin/seunjang
    }

    private var state = RecordState.BEFORE_RECORDING
        set(value) { // setter 설정
            field = value // 실제 프로퍼티에 대입
            resetButton.isEnabled = (value == RecordState.AFTER_RECORDING || value == RecordState.ON_PLAYING)
            recordButton.updateIconWithState(value)
        }

    private var recorder: MediaRecorder? = null // 사용 하지 않을 때는 메모리해제 및  null 처리
    private var player: MediaPlayer? = null

<<<<<<< HEAD
    // 초기 state에 따른 recordButton.text 설정
     fun initViews() {
        recordButton.updateIconWithState(state)
    }

    // 현재 state에 따른 recordButton 눌렀을 때 작동할 메써드
     fun bindViews() {
=======
     fun initViews() {
         recordButton.updateIconWithState(state)
    }

     fun bindViews() {

>>>>>>> origin/seunjang
        recordButton.setOnClickListener {
            when (state) {
                RecordState.BEFORE_RECORDING -> {
                    startRecoding()
                }
                RecordState.ON_RECORDING -> {
                    stopRecording()
                }
                RecordState.AFTER_RECORDING -> {
                    startPlaying()
                }
                RecordState.ON_PLAYING -> {
                    stopPlaying()
                }
            }
        }

        resetButton.setOnClickListener {
            stopPlaying()
            // clear
            recordTimeTextView.clearCountTime()
<<<<<<< HEAD
            recordDurationTextView.clearCountTime()
=======
>>>>>>> origin/seunjang
            state = RecordState.BEFORE_RECORDING
        }
    }

<<<<<<< HEAD
    // 처음 state init(BEFORE_RECORDING)으로 설정
=======
>>>>>>> origin/seunjang
    fun initVariables() {
        state = RecordState.BEFORE_RECORDING
    }

<<<<<<< HEAD
    // 로컬에 저장된 녹음파일 캐시주소
    // QuestionFragments로 전달, firebase storage에 3pg 파일형태로 저장될 예정
=======
>>>>>>> origin/seunjang
    fun returnRecordingFile() : String {
        return recordingFilePath
    }

<<<<<<< HEAD

    // 녹음 메써드
    fun startRecoding() {
=======
    private fun startRecoding() {
>>>>>>> origin/seunjang

        // 녹음 시작 시 초기화
        recorder = MediaRecorder()
            .apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) // 포멧
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // 엔코더
                setOutputFile(recordingFilePath) // 우리는 저장 x 캐시에
                prepare()
            }
        recorder?.start()
<<<<<<< HEAD
        recordDurationTextView.startCountUp()
=======
>>>>>>> origin/seunjang
        recordTimeTextView.startCountUp()
        state = RecordState.ON_RECORDING
    }

<<<<<<< HEAD
    // '녹음 중'일때 버튼 누를경우, 녹음 중단 및 메모리해제
    fun stopRecording() {
=======
    private fun stopRecording() {
>>>>>>> origin/seunjang
        recorder?.run {
            stop()
            release()
        }
        recorder = null
<<<<<<< HEAD
        recordDurationTextView.setRecordDuration(recordingFilePath)
=======
>>>>>>> origin/seunjang
        recordTimeTextView.stopCountUp()
        state = RecordState.AFTER_RECORDING
    }

<<<<<<< HEAD
    // 캐시에 저장된 녹음파일 실행
    fun startPlaying() {
=======
    private fun startPlaying() {
>>>>>>> origin/seunjang
        // MediaPlayer
        player = MediaPlayer()
            .apply {
                setDataSource(recordingFilePath)
                prepare() // 재생 할 수 있는 상태 (큰 파일 또는 네트워크로 가져올 때는 prepareAsync() )
            }

        // 전부 재생 했을 때
        player?.setOnCompletionListener {
            stopPlaying()
            state = RecordState.AFTER_RECORDING
        }

        player?.start() // 재생
        recordTimeTextView.startCountUp()
        state = RecordState.ON_PLAYING
    }

<<<<<<< HEAD
    fun stopPlaying() {
=======
    private fun stopPlaying() {
>>>>>>> origin/seunjang
        player?.release()
        player = null
        recordTimeTextView.stopCountUp()

        state = RecordState.AFTER_RECORDING
    }

    // 상수로 우리가 요청할 오디오 권한의 코드를 따로 정의
    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}