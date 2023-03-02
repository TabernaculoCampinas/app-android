package br.org.tabernaculocampinas.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.R
import br.org.tabernaculocampinas.databinding.FragmentPrayRequestBinding
import br.org.tabernaculocampinas.model.tabernacle.PrayRequest
import br.org.tabernaculocampinas.service.RetrofitHelper
import br.org.tabernaculocampinas.service.tabernacle.PrayRequestService
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrayRequestFragment : Fragment() {
    private lateinit var binding: FragmentPrayRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrayRequestBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        configureStateAutoComplete()
        configureSaveButton()
    }

    private fun configureStateAutoComplete() {
        val arrayAdapter =
            ArrayAdapter(
                requireContext(), R.layout.spinner_text, resources.getStringArray(R.array.listaUfs)
            )

        binding.editPrayRequestState.setAdapter(arrayAdapter)
    }

    private fun configureSaveButton() {
        with(binding) {
            buttonSaveClient.setOnClickListener {
                val retrofitHelper = RetrofitHelper.createRetrofitInstanceTabernacle()
                val request = PrayRequest(
                    editPrayRequestName.text.toString(),
                    editPrayRequestPhone.text.toString(),
                    editPrayRequestEmail.text.toString(),
                    editPrayRequestCity.text.toString(),
                    editPrayRequestState.text.toString(),
                    editPrayRequest.text.toString()
                )

                val endpoint = retrofitHelper.create(PrayRequestService::class.java)
                val callback =
                    endpoint.addPrayRequest(request, Credentials.basic("app", "Sg9LKdmnAOwbKbbp"))

                callback.enqueue(object :
                    Callback<br.org.tabernaculocampinas.model.tabernacle.Response> {
                    override fun onFailure(
                        call: Call<br.org.tabernaculocampinas.model.tabernacle.Response>,
                        t: Throwable
                    ) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<br.org.tabernaculocampinas.model.tabernacle.Response>,
                        response: Response<br.org.tabernaculocampinas.model.tabernacle.Response>
                    ) {
                        if (response.isSuccessful) {
                            val alert = AlertDialog.Builder(requireContext())
                            alert.setTitle(resources.getString(R.string.alert_success))
                            alert.setMessage(resources.getString(R.string.alert_pray_request))
                            alert.setPositiveButton(resources.getString(R.string.alert_ok_button)) { dialogInterface, _ ->
                                clearFields()
                                dialogInterface.dismiss()
                            }

                            alert.show()
                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    private fun clearFields() {
        with(binding) {
            editPrayRequestName.text = null
            editPrayRequestPhone.text = null
            editPrayRequestEmail.text = null
            editPrayRequestCity.text = null
            editPrayRequestState.text = null
            editPrayRequest.text = null
        }
    }
}