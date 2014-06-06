import java.lang.Math;
import java.awt.Color;


//
//  �m�����z�Ɋ�Â�臒l�̌v�Z�N���X
//
class  ThresholdByProbability extends ThresholdByAverage
{
	// �m�����z
	float[]  probability0;
	float[]  probability1;
	

	// 臒l�̌�����@�̖��O��Ԃ�
	public String  getThresholdName()
	{
		return  "���m���ɂȂ�悤臒l������";
	}
	
	// ���O���[�v�̓����ʂ���臒l�����肷��
	public void  determine( float[] features0, float[] features1 )
	{
		// ���N���X�iThresholdByAverage�j��臒l�v�Z���������s�i�����l�Ƃ��Ďg�p���邽�߁j
		super.determine( features0, features1 );
		
		// �Q�̓����ʂ̓x�����z�i�q�X�g�O�����j���v�Z
		makeHistogramsBySize( default_histogram_size );
		
		// �ݐϓx�����z����m�����z���v�Z
		makeProblility();
		
		// �e�אڋ�ԁii�Ԗڂ̋�Ԃ�i+1�Ԗڂ̋�Ԃ̊Ԃ̋�ԁj���ƂɁA
		// �o���m�����������Ȃ�_�����邩�ǂ����𒲂ׂ�
		// �����ʂ̓������Ȃ�_�������̋�Ԃő��݂���ꍇ�́A���ϒl���狁�߂�臒l�ɍł��߂����̂�
		// �ŏI�I��臒l�Ƃ���
		float  new_threshold;
		float  min_new_threshold = histogram_min_f; // �����l�Ƃ��ēK���Ȓl�����Ă���
		for ( int seg_no=0; seg_no<histogram0.length-1; seg_no++ )
		{
			// ��Ԃ̉E�[�E���[�̓����ʂ̒l���v�Z����
			float  feature_left, feature_right;
			feature_left = histogram_min_f + histogram_delta_f * ( seg_no + 0.5f );
			feature_right = histogram_min_f + histogram_delta_f * ( seg_no + 1.5f );
			
			// ��Ԃ̉E�[�E���[�ł̊e�O���[�v�̏o���m�����擾����
			float  prob0_left, prob1_left, prob0_right, prob1_right;
			prob0_left = probability0[ seg_no ];
			prob1_left = probability1[ seg_no ];
			prob0_right = probability0[ seg_no + 1 ];
			prob1_right = probability1[ seg_no + 1 ];
			
			// �E�[�E���[�ŏo���m���̍����O���[�v���قȂ��Ă���A
			// �������͂ǂ��炩�ŏo���m������������΁A
			// ���̋�ԂŕK���o���m�����������_�����݂���
			if ( /* �v���� */ )
			{
				// ��ԓ��̏o���m�����������_���v�Z����
				// �v����
				threshold = ...;
			}
		}

		// �V����臒l��ݒ�		
		threshold = min_new_threshold;
	}

	// ������Ԃ̃f�[�^���O���t�ɕ`��i�O���t�I�u�W�F�N�g�ɐ}�`�f�[�^��ݒ�j
	public void  drawGraph( GraphViewer gv )
	{
		// �f�[�^���z���U�z�}�ŕ`��
		drawScatteredGraph( gv, 0.0f, -0.02f );
		
		// �m�����z��܂���O���t�ŕ`��
		drawProbability( gv );
		
		// 臒l��`��
		drawThreshold( gv );
	}


	//
	//  臒l�v�Z�̂��߂̓������\�b�h
	//
	
	// �ݐϓx�����z����m�����z���v�Z
	protected void  makeProblility()
	{
		probability0 = new float[ histogram0.length ];
		for ( int i=0; i<probability0.length; i++ )
			probability0[ i ] = (float) histogram0[ i ] / features0.length;
		
		probability1 = new float[ histogram1.length ];
		for ( int i=0; i<probability0.length; i++ )
			probability1[ i ] = (float) histogram1[ i ] / features1.length;
	}
	

	//
	//  ������ԕ`��̂��߂̓������\�b�h
	//
	
	// �m�����z��܂���O���t�ŕ`��
	protected void  drawProbability( GraphViewer gv )
	{
		// �m�����z�O���t��`��
		GraphPoint  data[];
		data = new GraphPoint[ histogram0.length ];
		int  i;
		for ( i=0; i<histogram0.length; i++ )
		{
			data[ i ] = new GraphPoint();
			data[ i ].x = histogram_min_f + histogram_delta_f * ( i + 0.5f );
			data[ i ].y = probability0[ i ];
		}
		gv.addFigure( GraphViewer.FIG_LINE, Color.RED, data );

		data = new GraphPoint[ histogram1.length ];
		for ( i=0; i<histogram1.length; i++ )
		{
			data[ i ] = new GraphPoint();
			data[ i ].x = histogram_min_f + histogram_delta_f * ( i + 0.5f );
			data[ i ].y = probability1[ i ];
		}
		gv.addFigure( GraphViewer.FIG_LINE, Color.BLUE, data );
	}
}