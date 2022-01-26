package com.romes.voicerecorderapp

import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.romes.voicerecorderapp.databinding.RecordLayoutBinding
import java.io.IOException
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navController:NavController
    private lateinit var binding : RecordLayoutBinding
    var isRecording:Boolean = false
    private var recordPermissions : String = android.Manifest.permission.RECORD_AUDIO
    private val PERMISSION_CODE:Int = 21
    private var mediaRecorder: MediaRecorder?= null
    private lateinit var recordFile : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RecordLayoutBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recordedList.setOnClickListener{view: View->
            view.findNavController().navigate(R.id.action_recordFragment_to_listFragment)
        }
        binding.recordButton.setOnClickListener{
            if(isRecording){
                //Stop Recording
               stopRecording()
                binding.recordButton.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.record_button,null))
                isRecording=false

            }
            else{
                //Start Recording
                if(checkPermission()){
                startRecording()
                binding.recordButton.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.stop_button,null))
                isRecording = true}

            }
        }
    }

    private fun startRecording() {
        binding.recordTimer.base = SystemClock.elapsedRealtime()
        binding.recordTimer.start()
        val filePath : String = context?.getExternalFilesDir(null)!!.absolutePath
        val formatter : SimpleDateFormat = SimpleDateFormat("yyyy-mm-dd-hh-mm-ss", Locale.CANADA)
        val now : Date = Date()

        recordFile = "Recording" + formatter.format(now) + ".m4a"
        mediaRecorder = MediaRecorder().apply{
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setOutputFile(filePath + "/" + recordFile)
            setAudioEncodingBitRate(384000);
            setAudioSamplingRate(48000);

            setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("checking", "prepare() failed")
            }
       start()}

    }

    private fun stopRecording() {
        binding.recordTimer.stop()
        mediaRecorder?.apply {
        stop()
       reset()
        release()}
        mediaRecorder = null



    }

    private fun checkPermission(): Boolean {
        if(context?.let { ActivityCompat.checkSelfPermission(it,recordPermissions) } ==PackageManager.PERMISSION_GRANTED){
            return true
        }
        else{
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(recordPermissions),PERMISSION_CODE) }
            return false
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}