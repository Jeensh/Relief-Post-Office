package com.seoul42.relief_post_office.safety

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.seoul42.relief_post_office.R
import com.seoul42.relief_post_office.adapter.GetGuardianSafetyRVAdapter
import com.seoul42.relief_post_office.adapter.SafetyAdapter
import com.seoul42.relief_post_office.model.SafetyDTO

class GetGuardianSafetyActivity : AppCompatActivity() {

    private val database = Firebase.database
    private var safetyList = arrayListOf<Pair<String, SafetyDTO>>()
    private lateinit var auth : FirebaseAuth
    private lateinit var getGuardianSafetyAdapter : GetGuardianSafetyRVAdapter
    private lateinit var owner : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_guardian_safety)

        // 로그인 한 사람 uid 가져오기
        auth = Firebase.auth
        owner = auth.currentUser?.uid.toString()

        // safetyList 세팅
        setSafetyList()

        // 리사이클러 뷰 세팅
        setRecyclerView()

        /* 안부 추가 버튼 세팅 */
        setAddSafety()
    }

    /* 안부 추가 버튼 세팅 */
    private fun setAddSafety() {
        val safetyAddBtn = findViewById<ImageView>(R.id.get_guardian_safety_button)
        safetyAddBtn.setOnClickListener{
            startActivity(Intent(this, SafetyMake::class.java))
        }
    }

    private fun setSafetyList() {
        // 로그인한 유저의 안부 목록
        val userSafetyRef = database.getReference("guardian").child(owner).child("safetyList")

        // safetyList에 로그인한 유저의 안부들 넣기
        userSafetyRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // 로그인한 유저의 안부 하나씩 참조
                val safetyId = snapshot.key.toString()
                val safetyToAdd = database.getReference("safety").child(safetyId)

                // 안부 컬렉션에서 각 안부 불러와서 safetyList에 넣기
                safetyToAdd.get().addOnSuccessListener {
                    safetyList.add(Pair(safetyId, it.getValue(SafetyDTO::class.java) as SafetyDTO))
                    // 리사이클러 뷰 어댑터에 알려주기
                    getGuardianSafetyAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // safetyId 찾기
                val safetyId = snapshot.key.toString()
                val safetyInDB = database.getReference("safety").child(safetyId)

                // safetyList에서 safetyId에 해당하는 안부 찾아 수정해주기
                var idx = -1
                for (s in safetyList){
                    idx++
                    if (s.first == safetyId){
                        safetyInDB.get().addOnSuccessListener {
                            safetyList.remove(s)
                            safetyList.add(idx, Pair(safetyId, it.getValue(SafetyDTO::class.java) as SafetyDTO))

                            // 리스트가 수정되었다고 어댑터에게 알려주기
                            getGuardianSafetyAdapter.notifyDataSetChanged()
                        }
                        break
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // safetyId 찾기
                val safetyId = snapshot.key.toString()

                // safetyList에서 safetyId에 해당하는 안부 찾아 삭제하기
                for (s in safetyList){
                    if (s.first == safetyId) {
                        safetyList.remove(s)
                        // 리스트가 수정되었다고 어댑터에게 알려주기
                        getGuardianSafetyAdapter.notifyDataSetChanged()
                        break
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    // 리사이클러 뷰 세팅 함수
    private fun setRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.get_guardian_safety_rv)
        val layout = LinearLayoutManager(this)
        getGuardianSafetyAdapter = GetGuardianSafetyRVAdapter(this, safetyList)

        recyclerView.adapter = getGuardianSafetyAdapter
        recyclerView.layoutManager = layout
        recyclerView.setHasFixedSize(true)
    }
}