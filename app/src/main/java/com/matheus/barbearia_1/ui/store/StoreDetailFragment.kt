package com.matheus.barbearia_1.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.matheus.barbearia_1.R


class StoreDetailFragment : Fragment() {

    private lateinit var storeNameTextView: TextView
    private lateinit var storeAddressTextView: TextView
    private lateinit var storeDescriptionTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        storeNameTextView = view.findViewById(R.id.storeNameTextView)
        storeAddressTextView = view.findViewById(R.id.storeAddressTextView)
        storeDescriptionTextView = view.findViewById(R.id.storeDescriptionTextView)


        val storeName = arguments?.getString("storeName")
        val storeAddress = arguments?.getString("storeEndereco")
        val storeDescription = arguments?.getString("storeEmail")


        storeNameTextView.text = storeName
        storeAddressTextView.text = storeAddress
        storeDescriptionTextView.text = storeDescription



    }
}
