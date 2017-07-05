package sffmobile.cesar.com.br.sffmobile;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MovimentacaoFinanceira extends GenericEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String descricao;

	private Character tipoMovimentacao;

	private double valor;

	private double juros;

	private double multa;

	private double desconto;

	private Character situacao;

	private Date dataPrevista;

	private Date dataRealizada;

	private Date dataLancamento;

	private TipoGasto tipoGasto;

	private boolean manual = false;
	
	private Long tipoGastoId;
	
	private Long usuarioId;
	
	private Long saidaFixaId;
	
	private Long saidaVariavelId;
	
	private Long entradaVariavelId;
	
	private Long entradaFixaId;

	public static MovFinancComparator comparator = new MovFinancComparator();

	public MovimentacaoFinanceira() {

	}

	public MovimentacaoFinanceira(Long id, String descricao,
			Character tipoMovimentacao, double valor, Date dataPrevista,
			Date dataRealizada, Character situacao) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.tipoMovimentacao = tipoMovimentacao;
		this.valor = valor;
		this.dataPrevista = dataPrevista;
		this.dataRealizada = dataRealizada;
		this.situacao = situacao;

	}

	public double getSaldo() {

		return (this.valor + this.juros + this.multa - this.desconto);
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Character getSituacao() {
		return situacao;
	}

	public void setSituacao(Character situacao) {
		this.situacao = situacao;
	}

	public Character getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(Character tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public double getJuros() {
		return juros;
	}

	public void setJuros(double juros) {
		this.juros = juros;
	}

	public double getMulta() {
		return multa;
	}

	public void setMulta(double multa) {
		this.multa = multa;
	}

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public Date getDataPrevista() {
		return dataPrevista;
	}

	public void setDataPrevista(Date dataPrevista) {
		this.dataPrevista = dataPrevista;
	}

	public Date getDataRealizada() {
		return dataRealizada;
	}

	public void setDataRealizada(Date dataRealizada) {
		this.dataRealizada = dataRealizada;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public TipoGasto getTipoGasto() {
		return tipoGasto;
	}

	public void setTipoGasto(TipoGasto tipoGasto) {
		this.tipoGasto = tipoGasto;
	}

	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

	@Override
	public boolean isDuplicatedEntity() {
		return false;
	}
	

	public Long getTipoGastoId() {
		return tipoGastoId;
	}

	public void setTipoGastoId(Long tipoGastoId) {
		this.tipoGastoId = tipoGastoId;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Long getSaidaFixaId() {
		return saidaFixaId;
	}

	public void setSaidaFixaId(Long saidaFixaId) {
		this.saidaFixaId = saidaFixaId;
	}

	public Long getSaidaVariavelId() {
		return saidaVariavelId;
	}

	public void setSaidaVariavelId(Long saidaVariavelId) {
		this.saidaVariavelId = saidaVariavelId;
	}

	public Long getEntradaVariavelId() {
		return entradaVariavelId;
	}

	public void setEntradaVariavelId(Long entradaVariavelId) {
		this.entradaVariavelId = entradaVariavelId;
	}

	public Long getEntradaFixaId() {
		return entradaFixaId;
	}

	public void setEntradaFixaId(Long entradaFixaId) {
		this.entradaFixaId = entradaFixaId;
	}

	public static MovimentacaoFinanceira findMovimentacaoFinanceira(List<MovimentacaoFinanceira> movimentacaoFinanceiraList,
			Long movFianncId) {
		MovimentacaoFinanceira movFinanc = null;

		for (MovimentacaoFinanceira item : movimentacaoFinanceiraList) {

			if (item.getId().equals(movFianncId)) {
				movFinanc = item;
			}
		}

		return movFinanc;

	}
	


	static class MovFinancComparator implements
			Comparator<MovimentacaoFinanceira> {

		@Override
		public int compare(MovimentacaoFinanceira lhs,
				MovimentacaoFinanceira rhs) {

			int i = 0;
			
			
			i = compareSituacao(lhs.getSituacao(), rhs.getSituacao());
			
			
			if (lhs.getDataRealizada() != null
					&& rhs.getDataRealizada() != null) {
				i = lhs.getDataRealizada()
						.compareTo(rhs.getDataRealizada());
			}

			if (i == 0) {
				i = lhs.getDataPrevista().compareTo(rhs.getDataPrevista());
			}
			
			if(i ==0){
				i =  (int) (Long.valueOf(lhs.getId()) - Long.valueOf(rhs.getId()));
			}

			return i;
		}
		
		
		private int compareSituacao(Character lhs, Character rhs){

			int lhsValue = 0;
			int rhsValue = 0;
			
			if(lhs.equals('R'))
				lhsValue = 0;
			else
				lhsValue = 1;
			
			if(rhs.equals('R'))
				rhsValue = 0;
			else
				rhsValue = 1;
			
			return lhsValue - rhsValue;
			
		}

	}

	public static MovFinancComparator getComparator() {
		return comparator;
	}

	public static void setComparator(MovFinancComparator comparator) {
		MovimentacaoFinanceira.comparator = comparator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
