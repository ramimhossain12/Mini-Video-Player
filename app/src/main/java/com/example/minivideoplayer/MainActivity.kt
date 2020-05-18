package com.example.minivideoplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minivideoplayer.Adapter.VideoAdapter
import com.example.minivideoplayer.MainActivity
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    var myRecyclerView: RecyclerView? = null
    var obj_adapter: VideoAdapter? = null
    var directory: File? = null
    var bolean_permission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myRecyclerView = findViewById<View>(R.id.listVideoRecycler) as RecyclerView
        //Phone Memory And SD Card
        directory = File("/mnt/")
        //SD Card
        directory = File("/storage/")
        val manager = GridLayoutManager(this@MainActivity, 2)
        myRecyclerView!!.layoutManager = manager
        permissionForVideo()
    }

    private fun permissionForVideo() {
        if (ContextCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_PERMISSION)
            }
        } else {
            bolean_permission = true
            getFile(directory)
            obj_adapter = VideoAdapter(applicationContext, fileArrayList)
            myRecyclerView!!.adapter = obj_adapter
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bolean_permission = true
                getFile(directory)
                obj_adapter = VideoAdapter(applicationContext, fileArrayList)
                myRecyclerView!!.adapter = obj_adapter
            } else {
                Toast.makeText(this, "Please Allow the Permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getFile(directory: File?): ArrayList<File> {
        val listFile = directory!!.listFiles()
        if (listFile != null && listFile.size > 0) {
            for (i in listFile.indices) {
                if (listFile[i].isDirectory) {
                    getFile(listFile[i])
                } else {
                    bolean_permission = false
                    if (listFile[i].name.endsWith(".mp4")) {
                        for (j in fileArrayList.indices) {
                            if (fileArrayList[j].name == listFile[i].name) {
                                bolean_permission = true
                            } else {
                            }
                        }
                        if (bolean_permission) {
                            bolean_permission = false
                        } else {
                            fileArrayList.add(listFile[i])
                        }
                    }
                }
            }
        }
        return fileArrayList
    }

    companion object {
        var REQUEST_PERMISSION = 1
        @JvmField
        var fileArrayList = ArrayList<File>()
    }
}