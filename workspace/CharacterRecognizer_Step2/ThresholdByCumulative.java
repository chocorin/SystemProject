import java.awt.Color;


//
//	�ݐϕ��z�Ɋ�Â�臒l�̌v�Z�N���X
//
class  ThresholdByCumulative extends ThresholdByProbability
{
	// �ݐϕ��z
	float[]  cumulative0;
	float[]  cumulative1;
	

	// 臒l�̌�����@�̖��O��Ԃ�
	public String  getThresholdName()
	{
		return  "��F�������������Ȃ�悤��臒l������";
	}

	// ���O���[�v�̓����ʂ���臒l�����肷��
	public void  determine( float[] features0, float[] features1 )
	{
		// ���N���X�iThresholdByAverage�j��臒l�v�Z���������s�i�m�����z�܂ł��v�Z�j
		super.determine( features0, features1 );
		
		// �m�����z����ݐϕ��z���v�Z
		makeCumulative();

		
		// �e��Ԃ��Ƃɗݐϕ��z�̘a�� 1.0 �ɂȂ�_�����邩�ǂ����𒲂ׂ�
		float  new_threshold;
		for ( int seg_no=0; seg_no<histogram0.length-1; seg_no++ )
		{
			// ��Ԃ̉E�[�E���[�̗ݐϕ��z�𒲂ׂ�
			// �v����
			
			// ���[�̗ݐϕ��z��1.0��菬�����A�E�[�̗ݐϕ��z��1.0�ȏ�ł���΁A
			// ���̋�ԂŕK���ݐϕ��z�̘a��1.0�ɂȂ�_�����݂���
			if ( /* �v���� */ )
			{
				// ��ԓ��̏o���m�����������_���v�Z����
				// �v����
				threshold = ...;
			}
		}
	}

	// ������Ԃ̃f�[�^���O���t�ɕ`��i�O���t�I�u�W�F�N�g�ɐ}�`�f�[�^��ݒ�j
	public void  drawGraph( GraphViewer gv )
	{
		// �ݐϕ��z��܂���O���t�ŕ`��
		drawCumulative( gv );
		
		// �f�[�^���z���U�z�}�ŕ`��
		drawScatteredGraph( gv, 0.0f, -0.1f );
		
		// 臒l��`��
		drawThreshold( gv );
	}
	

	//
	//  臒l�v�Z�̂��߂̓������\�b�h
	//
	
	// �m�����z����ݐϕ��z���v�Z
	protected void  makeCumulative()
	{
		cumulative0 = new float[ probability0.length ];
		cumulative0[ 0 ] = probability0[ 0 ];
		for ( int i=1; i<cumulative0.length; i++ )
			cumulative0[ i ] = cumulative0[ i-1 ] + probability0[ i ];
			
		cumulative1 = new float[ probability1.length ];
		cumulative1[ 0 ] = probability1[ 0 ];
		for ( int i=1; i<cumulative1.length; i++ )
			cumulative1[ i ] = cumulative1[ i-1 ] + probability1[ i ];
	}	
	

	//
	//  ������ԕ`��̂��߂̓������\�b�h
	//
	
	// �ݐϕ��z��܂���O���t�ŕ`��
	protected void  drawCumulative( GraphViewer gv )
	{
		// �m�����z�O���t��`��
		GraphPoint  data[];
		data = new GraphPoint[ histogram0.length ];
		int  i;
		for ( i=0; i<histogram0.length; i++ )
		{
			data[ i ] = new GraphPoint();
			data[ i ].x = histogram_min_f + histogram_delta_f * ( i + 0.5f );
			data[ i ].y = cumulative0[ i ];
		}
		gv.addFigure( GraphViewer.FIG_LINE, Color.RED, data );

		data = new GraphPoint[ histogram1.length ];
		for ( i=0; i<histogram1.length; i++ )
		{
			data[ i ] = new GraphPoint();
			data[ i ].x = histogram_min_f + histogram_delta_f * ( i + 0.5f );
			data[ i ].y = cumulative1[ i ];
		}
		gv.addFigure( GraphViewer.FIG_LINE, Color.BLUE, data );
	}
}


