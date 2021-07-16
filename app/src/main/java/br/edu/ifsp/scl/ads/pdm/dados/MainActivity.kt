package br.edu.ifsp.scl.ads.pdm.dados

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.scl.ads.pdm.dados.SettingsActivity.Constantes.NUMERO_DADOS_ATRIBUTO
import br.edu.ifsp.scl.ads.pdm.dados.SettingsActivity.Constantes.NUMERO_FACES_ATRIBUTO
import br.edu.ifsp.scl.ads.pdm.dados.databinding.ActivityMainBinding
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var gerarRandomico: Random
    private lateinit var settingsActivityLauncher:ActivityResultLauncher<Intent>
    private var faces: Int = 6;
    private var dados: Int = 1;
    private lateinit var  configuracoesSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        gerarRandomico = Random(System.currentTimeMillis())

        configuracoesSharedPreferences = getSharedPreferences(SettingsActivity.Constantes.CONFIGURACOES_ARQUIVO, MODE_PRIVATE)
        dados = configuracoesSharedPreferences.getInt(NUMERO_DADOS_ATRIBUTO, 1)
        faces = configuracoesSharedPreferences.getInt(NUMERO_FACES_ATRIBUTO, 6)

        activityMainBinding.jogarDadosBt.setOnClickListener{
            if(dados == 1) {
                val resultado: Int = gerarRandomico.nextInt(1..faces)
                "A face sorteada foi $resultado".also { activityMainBinding.resultadoTv.text = it }
                val nomeImagem = "dice_$resultado"
                activityMainBinding.resultadoIv.setImageResource(
                        resources.getIdentifier(nomeImagem, "mipmap", packageName)
                )
            }else{
                val resultado1: Int = gerarRandomico.nextInt(1..faces)
                val resultado2: Int = gerarRandomico.nextInt(1..faces)
                "A face sorteada foi $resultado1 e $resultado2".also { activityMainBinding.resultadoTv.text = it }
            }
        }

        settingsActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == RESULT_OK){
                if(result.data != null){
                    val configuracao: Configuracao? = result.data?.getParcelableExtra<Configuracao>(Intent.EXTRA_USER)
                    if (configuracao != null) {
                        faces = 0
                        faces += configuracao.numeroFaces.toString().toInt()
                        dados = 0
                        dados += configuracao.numeroDados.toString().toInt()
                    }
                }
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settingsMi){
            val settingsIntent = Intent(this,SettingsActivity::class.java)
            settingsActivityLauncher.launch(settingsIntent)
            return true
        }
        return false
    }
}