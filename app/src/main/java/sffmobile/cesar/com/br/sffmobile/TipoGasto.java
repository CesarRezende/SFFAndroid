package sffmobile.cesar.com.br.sffmobile;

import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class TipoGasto extends GenericEntity implements  Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String KEY_ID = "id";

	private static final String KEY_VERSAO = "versao";

	private static final String KEY_DESCRICAO = "descricao";

	private static final String KEY_DESATIVADO = "desativado";

	private Long id;

	private Long versao;

	private String descricao;

	private boolean desativado;

	private static TipoGastoComparator comparator = new TipoGastoComparator();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isDesativado() {
		return desativado;
	}

	public void setDesativado(boolean desativado) {
		this.desativado = desativado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static TipoGastoComparator getComparator() {
		return comparator;
	}

	private static class TipoGastoComparator implements Comparator<TipoGasto> {

		@Override
		public int compare(TipoGasto arg0, TipoGasto arg1) {

			return arg0.getDescricao().compareTo(arg1.getDescricao());
		}

	}

	public static TipoGasto findTipoGasto(List<TipoGasto> tipoGastoList,
			Long tipoGastoId) {
		TipoGasto tipoGasto = null;

		for (TipoGasto item : tipoGastoList) {

			if (item.getId().equals(tipoGastoId)) {
				tipoGasto = item;
			}
		}

		return tipoGasto;

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();

		bundle.putLong(KEY_ID, this.getId());
		bundle.putLong(KEY_VERSAO, this.getVersao());
		bundle.putString(KEY_DESCRICAO, this.getDescricao());
		bundle.putBoolean(KEY_DESATIVADO, this.isDesativado());

		dest.writeBundle(bundle);

	}

	public static final Creator<TipoGasto> CREATOR = new Creator<TipoGasto>() {

		@Override
		public TipoGasto createFromParcel(Parcel source) {
			Bundle bundle = source.readBundle();
			TipoGasto tipoGasto = new TipoGasto();
			tipoGasto.setId(bundle.getLong(KEY_ID));
			tipoGasto.setVersao(bundle.getLong(KEY_VERSAO));
			tipoGasto.setDescricao(bundle.getString(KEY_DESCRICAO));
			tipoGasto.setDesativado(bundle.getBoolean(KEY_DESATIVADO));

			return tipoGasto;
		}

		@Override
		public TipoGasto[] newArray(int size) {
			return new TipoGasto[size];
		}

	};

	@Override
	public boolean isDuplicatedEntity() {
		return false;
	}

}
