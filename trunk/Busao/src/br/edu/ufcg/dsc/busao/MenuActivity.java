package br.edu.ufcg.dsc.busao;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import br.com.indigo.android.facebook.SocialFacebook;
import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.exception.NoReturnDataException;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import br.edu.ufcg.dsc.util.CustomBuilder;
import br.edu.ufcg.dsc.util.PontoAdapter;
import br.edu.ufcg.dsc.util.PontoTuristico;

public class MenuActivity extends Activity {

	TableRow rowLocalidade, rowBuscar, rowTurismo, rowAjuda, rowLogoBusao;
	ViewGroup includePrincipal;
	LinearLayout linearLayoutScrollView;
	ImageView botaoAlterarCidade, botaoSearch, botaoFace, botaoTwitter;
	HTTPModuleFacade service;
	private Map<String, String> cidadesApp = new HashMap<String, String>();
	private View bodyResult;
	private Spinner spinnerCidades;
	private ListView list;
	private List<PontoTuristico> pontos;
	private static ImageView bolinha0;
	private static ImageView bolinha1;
	private static ImageView bolinha2;
	private static ImageView bolinha3;
	public static Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			default:
				Log.i("Menu", "" + msg.what);
				atualBolinhas(msg.what);
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setTextFont();
		MultiDirectionSlidingDrawer drawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
		drawer.open();
		instanciaBolinhas();
		atualBolinhas(0);
		// scroll.setItemWidth(width);
		instanciarRows();
		setActionsRows(rowLocalidade, R.layout.menu_localidade);
		// setActionsRows(rowBuscar, R.layout.buscar_onibus);
		setActionRowBuscar();
		setActionsRows(rowTurismo, R.layout.menu_turismo);
		setActionsRows(rowAjuda, R.layout.menu_ajuda);
		setActionsRowLogo();
		service = HTTPModuleFacade.getInstance();

		Bundle b = getIntent().getExtras();
		if (b != null) {
			int rowClicada = b.getInt("row");
			Log.i("row", "" + rowClicada);
			switch (rowClicada) {
			case R.layout.menu_localidade:
				rowLocalidade.performClick();
				break;
			case R.layout.menu_turismo:
				rowTurismo.performClick();
				break;

			case R.layout.menu_ajuda:
				rowAjuda.performClick();
				break;

			default:
				break;
			}
		}
	}

	private void instanciaBolinhas() {
		bolinha0 = (ImageView) findViewById(R.id.image_bolinha_0);
		bolinha1 = (ImageView) findViewById(R.id.image_bolinha_1);
		bolinha2 = (ImageView) findViewById(R.id.image_bolinha_2);
		bolinha3 = (ImageView) findViewById(R.id.image_bolinha_3);

	}

	private static void fadeBolinhas(int valor) {
		bolinha0.setAlpha(valor);
		bolinha1.setAlpha(valor);
		bolinha2.setAlpha(valor);
		bolinha3.setAlpha(valor);
	}

	public static void atualBolinhas(int id) {
		fadeBolinhas(50);
		switch (id) {
		case 0:
			bolinha0.setAlpha(255);
			break;
		case 1:
			bolinha1.setAlpha(255);
			break;
		case 2:
			bolinha2.setAlpha(255);
			break;
		case 3:
			bolinha3.setAlpha(255);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void setActionAlterarCidade(ImageView botao) {
		if (botao == null)
			return;
		botao.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showDialog(1);
			}
		});
	}

	private void setActionRowBuscar() {
		rowBuscar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent buscarPrincipal = new Intent(MenuActivity.this,
						BuscarActivity.class);
				Bundle b = new Bundle();
				// b.put("paramBusca", paramBusca.getText().toString());
				buscarPrincipal.putExtras(b);
				MenuActivity.this.startActivity(buscarPrincipal);
				MenuActivity.this.finish();

			}
		});
	}

	private void setActionsRows(final TableRow row, final int layout) {
		row.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				limpaRows();
				row.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.transparencia));

				RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.includePrincipal);
				myLayout.removeAllViews();
				myLayout.addView(getLayoutInflater().inflate(layout, null));
				setAlteracoesTela(layout);
			}
		});

	}

	private void setActionsRowLogo() {
		rowLogoBusao.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showDialog(2);
			}
		});

	}

	private void setAlteracoesTela(int id) {
		switch (id) {
		case R.layout.menu_localidade:
			// alterar os dados...
			String valorTarifa = getCityValorTarifa();
			TextView endereco = (TextView) findViewById(R.id.endereco);
			Log.i("endereco", service.getAtualEndrereco());
			endereco.setText(getString(R.string.atual_localizacao) + " " + service.getAtualEndrereco());
			botaoAlterarCidade = (ImageView) findViewById(R.id.botao_alterar_cidade);
			setActionAlterarCidade(botaoAlterarCidade);
			TextView textTarifa = (TextView) findViewById(R.id.text_preco_tarifa);
			textTarifa.setText(textTarifa.getText() + " "
					+ valorTarifa);
			cidadesApp = service.getAllCitys();
			break;

		case R.layout.buscar_onibus:
			botaoSearch = (ImageView) findViewById(R.id.search);
			botaoSearch.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					showDialog(3);
				}
			});
			break;

		case R.layout.menu_turismo:
			criaListView();

			list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int pos, long id) {

					String latitude = pontos.get(pos).getLatitude();
					String longitude = pontos.get(pos).getLongitude();

					Intent telaConsultar = new Intent(MenuActivity.this,
							ResultadoActivity.class);
					Bundle b = new Bundle();
					b.putDouble("lat1", Double.parseDouble(latitude));
					b.putDouble("long1", Double.parseDouble(longitude));
					b.putDouble("lat2", 0.0);
					b.putDouble("long2", 0.0);

					telaConsultar.putExtras(b);
					startActivity(telaConsultar);
					return true;
				}

			});
		default:
			break;
		}
	}

	private void criaListView() {
		list = (ListView) findViewById(R.id.turismo_list);
		;
		pontos = new ArrayList<PontoTuristico>();
		PontoAdapter adapter;
		int imagem = 0;
		List<Map<String, String>> pontosTuri = service.getAllTuristicPoint();
		if (pontosTuri == null) {
			Toast.makeText(MenuActivity.this, getString(R.string.sem_conexao),
					Toast.LENGTH_LONG).show();
			return;
		}
		for (Map<String, String> map : pontosTuri) {

			if (map.get("nome").equalsIgnoreCase("Parque do Povo")) {
				imagem = R.drawable.pt_cg_pp;
			} else if (map.get("nome").equalsIgnoreCase(
					"Tropeiros da Borborema")) {
				imagem = R.drawable.pt_cg_tropeiros;
			} else if (map.get("nome").equalsIgnoreCase("Acude Velho")) {
				imagem = R.drawable.pt_cg_acudevelho;
			} else if (map.get("nome").equalsIgnoreCase("Jackson do Pandeiro")) {
				imagem = R.drawable.pt_cg_jackson;
			} else { // imagem default
				imagem = R.drawable.pt_default;
			}

			pontos.add(new PontoTuristico(map.get("id"), map.get("nome"), map
					.get("latitude"), map.get("longitude"), map
					.get("descricao"), imagem));
		}
		adapter = new PontoAdapter(this, pontos);
		list.setAdapter(adapter);
	}

	private void instanciarRows() {
		rowLocalidade = (TableRow) findViewById(R.id.rowLocalidade);
		rowBuscar = (TableRow) findViewById(R.id.rowBuscar);
		rowTurismo = (TableRow) findViewById(R.id.rowTurismo);
		rowAjuda = (TableRow) findViewById(R.id.rowAjuda);
		rowLogoBusao = (TableRow) findViewById(R.id.rowLogoBusao);

	}

	private void limpaRows() {
		rowLocalidade.setBackgroundDrawable(null);
		rowBuscar.setBackgroundDrawable(null);
		rowTurismo.setBackgroundDrawable(null);
		rowAjuda.setBackgroundDrawable(null);
	}

	/**
	 * Limpa a pilha de Views no Linear Layout
	 */
	public void limpaLinearLayout() {
		// LinearLayout scrollView =
		// (LinearLayout)findViewById(R.id.layout_menu_scrolling);
		// scrollView.removeAllViews();
	}

	private void setTextFont() {
		// set Font
		TextView textLocalidade = (TextView) findViewById(R.id.textLocalidade);
		TextView textBuscar = (TextView) findViewById(R.id.textBuscar);
		TextView textTurismo = (TextView) findViewById(R.id.textTurismo);
		TextView textAjuda = (TextView) findViewById(R.id.textAjuda);
		TextView textAlterarCidade = (TextView) findViewById(R.id.text_alterar_cidade);
		Typeface font = Typeface.createFromAsset(getAssets(), "font.TTF");
		textLocalidade.setTypeface(font);
		textBuscar.setTypeface(font);
		textTurismo.setTypeface(font);
		textAjuda.setTypeface(font);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.MyTheme);
		CustomBuilder builder = null;
		switch (id) {
		case 1:
			builder = new CustomBuilder(ctw, R.layout.popup_escolher_cidade);
			builder.setTitle(getString(R.string.titulo_alterar_cidade));
			builder.setIcon(R.drawable.seta_titulo);
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.botao_confirmar),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.setNegativeButton(getString(R.string.botao_cancelar),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;

		case 2:
			builder = new CustomBuilder(ctw, R.layout.about);
			builder.setTitle("");
			builder.setIcon(null);
			builder.setCancelable(false);
			builder.setNegativeButton(getString(R.string.botao_voltar),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;

		default:
			break;
		}
		dialog = builder.create();
		if (dialog == null) {
			dialog = super.onCreateDialog(id);
		}

		if (id == 1) {
			bodyResult = builder.getTemplateBody();
			// Identifica o Spinner no layout
			spinnerCidades = (Spinner) bodyResult
					.findViewById(R.id.spinner_cidade);
			// Cria um ArrayAdapter usando um padrão de layout da classe R do
			// android, passando o ArrayList nomes
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, new ArrayList(
							cidadesApp.values()));
			ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
			spinnerArrayAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerCidades.setAdapter(spinnerArrayAdapter);

			// Método do Spinner para capturar o item selecionado
			spinnerCidades
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

						private String nome;

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View v, int posicao, long id) {
							// pega nome pela posição
							nome = parent.getItemAtPosition(posicao).toString();
							// imprime um Toast na tela com o nome que foi
							// selecionado
							// Toast.makeText(ResultadoActivity.this,
							// "Nome Selecionado: " + nome,
							// Toast.LENGTH_LONG).show();
							// String idRota = getKeyByValue(cidadesApp, nome);
							// atualizaDados(idRota);
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
					});

		}
		return dialog;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SocialFacebook.getInstance().authorizeCallback(requestCode, resultCode,
				data);

	}
	
	public String getCityValorTarifa(){
		try {
			return service.getCityValorTarifa();
		} catch (NoReturnDataException no) {
			 Toast.makeText(MenuActivity.this,
			 getString(R.string.nenhum_dado_retornado),
			 Toast.LENGTH_LONG).show();
			no.printStackTrace();
			return "";
		} catch (Exception e) {
			 Toast.makeText(MenuActivity.this,
					 getString(R.string.sem_conexao),
					 Toast.LENGTH_LONG).show();
					e.printStackTrace();
					return "";
		}
		
	}

}