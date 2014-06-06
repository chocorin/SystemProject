import java.lang.Math;
import java.awt.Color;


//
//	��F�����̘a�Ɋ�Â�臒l�̌v�Z�N���X
//
class  ThresholdByMinimization extends ThresholdByCumulative
{
	// ���v�̌�F�����̕��z
	float[]  error_sum;


	// 臒l�̌�����@�̖��O��Ԃ�
	public String  getThresholdName()
	{
		return  "��F�������ŏ��ɂȂ�悤��臒l������";
	}

	// ���O���[�v�̓����ʂ���臒l�����肷��
	public void  determine( float[] features0, float[] features1 )
	{
		// ���N���X�iThresholdByCumulative�j��臒l�v�Z���������s�i�ݐϕ��z�܂ł��v�Z�j
		super.determine( features0, features1 );
		
		// �ݐϕ��z���獇�v�̌�F�����̕��z���v�Z
		makeErrorSum();

		
		// �e��Ԃ��Ƃɍ��v�̌�F�������ő�ɂȂ邩�ǂ����𒲂ׂāA�ő�̋�Ԃ�臒l�Ƃ���
		// �v����
		threshold = ...;
	}

	// ������Ԃ̃f�[�^���O���t�ɕ`��i�O���t�I�u�W�F�N�g�ɐ}�`�f�[�^��ݒ�j
	public void  drawGraph( GraphViewer gv )
	{
		// ���v�̌�F�����̕��z��܂���O���t�ŕ`��
		drawErrorSum( gv );
		
		// �f�[�^���z���U�z�}�ŕ`��
		drawScatteredGraph( gv, 0.0f, -0.1f );
		
		// 臒l��`��
		drawThreshold( gv );
	}


	//
	//  臒l�v�Z�̂��߂̓������\�b�h
	//
	
	// �ݐϕ��z���獇�v�̌�F�����̕��z���v�Z
	protected void  makeErrorSum()
	{
		error_sum = new float[ cumulative0.length ];
		for ( int i=0; i<cumulative0.length; i++ )
			error_sum[ i ] = Math.abs( cumulative0[ i ] - cumulative1[ i ] );
	}	
	

	//
	//  ������ԕ`��̂��߂̓������\�b�h
	//
	
	// ���v�̌�F�����̕��z��܂���O���t�ŕ`��
	protected void  drawErrorSum( GraphViewer gv )
	{
		// �m�����z�O���t��`��
		GraphPoint  data[];
		data = new GraphPoint[ error_sum.length ];
		int  i;
		for ( i=0; i<error_sum.length; i++ )
		{
			data[ i ] = new GraphPoint();
			data[ i ].x = histogram_min_f + histogram_delta_f * ( i + 0.5f );
			data[ i ].y = error_sum[ i ];
		}
		gv.addFigure( GraphViewer.FIG_LINE, Color.BLACK, data );
	}
}


