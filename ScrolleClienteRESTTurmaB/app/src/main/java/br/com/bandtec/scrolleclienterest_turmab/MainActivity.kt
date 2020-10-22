package br.com.bandtec.scrolleclienterest_turmab

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Criamos uma objeto do tipo TextView
        val novoTv = TextView(baseContext)

        // configurando a TextView
        novoTv.text = "texto em tempo de execução!"
        novoTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f) // 15sp
        novoTv.setTextColor(Color.parseColor("#FF0099")) // cor RGB

        // inserindo a TextView na LinearLayout
        ll_conteudo.addView(novoTv)

        // iria inserir no Layout Principal da tela
        // layout_principal.addView(novoTv)

        //apiTrazerTodos()
        //apiTrazerUm(2)
        apiCriarFilme()
    }
    fun criarRequisicoes():ApiFilmes {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://5f861cfdc8a16a0016e6aacd.mockapi.io/bandtec-api/")
            .build()
        val requisicoes = retrofit.create(ApiFilmes::class.java)

        return  requisicoes
    }

    fun apiCriarFilme(){
        val novoFilme = Filme (
            0,
            "Boa noite ate semana que vem!",
            2020,
        100.50.toBigDecimal(),
        true)
        val callNovoFilme =criarRequisicoes().postFilme(novoFilme)
        callNovoFilme.enqueue(object :Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(baseContext, "Cadastrado com Sucesso!", Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(baseContext, "Erro: $t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun apiTrazerTodos(){
        val callFilmes = criarRequisicoes().getFilmes()
        callFilmes.enqueue(object : Callback<List<Filme>> {
            override fun onResponse(call: Call<List<Filme>>, response: Response<List<Filme>>) {
                response.body()?.forEach {
                    val novoTv = TextView(baseContext)
                    novoTv.text = "\nFilme: ${it.nome}\nAno: ${it.ano} \nCusto: ${it.custoProducao}"
                    novoTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f) // 15sp
                    novoTv.setTextColor(Color.parseColor("#FF0000")) // cor RGB

                    ll_conteudo.addView(novoTv)
                }
            }
            override fun onFailure(call: Call<List<Filme>>, t: Throwable) {
                Toast.makeText(baseContext, "Erro: $t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun apiTrazerUm(id:Int){
        val callFilmes = criarRequisicoes().getFilme(id)
        callFilmes.enqueue(object : Callback<Filme> {
            override fun onResponse(call: Call<Filme>, response: Response<Filme>) {
                val novoTv = TextView(baseContext)
                val filme = response.body()
                novoTv.text =
                    "\nFilme: ${filme?.nome}\nAno: ${filme?.ano} \nCusto: ${filme?.custoProducao}"
                novoTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f) // 15sp
                novoTv.setTextColor(Color.parseColor("#FF0000")) // cor RGB

                ll_conteudo.addView(novoTv)
            }
            override fun onFailure(call: Call<Filme>, t: Throwable) {
                Toast.makeText(baseContext, "Erro: $t", Toast.LENGTH_SHORT).show()
            }
        })
    }
}