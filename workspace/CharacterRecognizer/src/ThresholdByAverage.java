import java.awt.Color;


//
//	���ϒl�Ɋ�Â�臒l�̌v�Z�N���X
//
class  ThresholdByAverage implements ThresholdDeterminer
{
	// 臒l�ƕ����i�O���[�v0�̕��������ʂ�臒l������������ΐ^�j
	protected float  threshold;
	protected boolean  is_first_smaller;
	
	// �����ʃf�[�^�i�O���t�`��p�j
	protected float  features0[];
	protected float  features1[];
	
	// �e�O���[�v�̓����ʂ̕��ϒl�i�O���t�`��p�j
	protected float  average0;
	protected float  average1;
	
	// �x�����z�i�q�X�g�O�����j
	protected float  histogram_min_f, histogram_max_f, histogram_delta_f;
	protected int[]  histogram0;
	protected int[]  histogram1;
	
	// �x�����z�̃f�t�H���g�̋�ԕ�
	float  default_histogram_delta = 0.02f;
	int    default_histogram_size = 20;
	
	
	// 臒l�̌�����@�̖��O��Ԃ�
	public String  getThresholdName()
	{
		return  "���ϒl�ɂ��臒l�̌���";
	}
	
	// ���O���[�v�̓����ʂ���臒l�����肷��
	public void  determine( float[] features0, float[] features1 )
	{
		// �e�O���[�v�̕��ϒl���v�Z
		average0 = 0.0f; // �v����
		average1 = 0.0f; // �v����
		
		float sum0 = 0;
		for(int i=0; i<features0.length-1; i++)
			sum0 += features0[i];
		average0 = sum0/features0.length;
		
		float sum1 = 0;
		for(int i=0; i<features1.length-1; i++)
			sum1 += features1[i];
		average1 = sum1/features1.length;
		
		// �Q�̕��ϒl�̒����l���v�Z
		threshold = (average0+average1)/2; // �v����
		
		// �������v�Z
		if(average0 < threshold)
			is_first_smaller = true; // �v����
		if(threshold < average0)
			is_first_smaller = false;
		
		// �����ʃf�[�^���L�^�i�O���t�`��p�j
		this.features0 = features0;
		this.features1 = features1;
	}

	// 臒l�����Ƃɓ����ʂ��當���𔻒肷��
	public int  recognize( float feature )
	{
		// �O���[�v0�̓����� < 臒l < �O���[�v1�̓�����
		if ( is_first_smaller )
		{
			if ( feature < threshold ) 
				return  0;
			else
				return  1;
		}
		// �O���[�v1�̓����� < 臒l < �O���[�v0�̓�����
		else
		{
			if ( feature < threshold ) 
				return  1;
			else
				return  0;
		}
	}
	
	// 臒l��Ԃ�
	public float  getThreshold()
	{
		return  threshold;
	}
	
	// ������Ԃ̃f�[�^���O���t�ɕ`��i�O���t�I�u�W�F�N�g�ɐ}�`�f�[�^��ݒ�j
	public void  drawGraph( GraphViewer gv )
	{
		// �Q�̓����ʂ̓x�����z�i�q�X�g�O�����j���v�Z
		//�i�q�X�g�O�������쐬������@�Ƃ��ĂQ�ʂ�p�ӂ���Ă���̂ŁA�ǂ��炩�K���ȕ����Ăяo���B�j
//		makeHistogramsByWidth( default_histogram_delta );
		makeHistogramsBySize( default_histogram_size );
		
		// �f�[�^���z���U�z�}�ŕ`��
		drawScatteredGraph( gv, 0.0f, -1.0f );
		
		// �x�����z��_�O���t�ŕ`��
		drawHistogram( gv );
		
		// ���ϒl���c���ŕ`��
		drawAverage( gv );
		
		// 臒l��`��
		drawThreshold( gv );
	}
	
	
	//
	//  臒l�v�Z�̂��߂̓������\�b�h
	//
	
	// �w�肳�ꂽ�͈́E��Ԃœ����ʂ̓x�����z�i�q�X�g�O�����j���v�Z
	//�imin_f, maxf �̓q�X�g�O�������쐬��������ʂ͈̔́Adelta_f �͊e��Ԃ��Ƃ̓����ʂ̕��j
	protected int[]  makeHistogram( float[] features, float min_f, float max_f, float delta_f )
	{
		// �q�X�g�O�����̋�Ԑ����v�Z���Ĕz���������
		int  histogram_size = (int) java.lang.Math.ceil( ( max_f - min_f ) / delta_f );
		int[]  histogram = new int[ histogram_size ];
		
		// �q�X�g�O�����̊e��Ԃɂ�����f�[�^�̏o���񐔂��J�E���g
		for ( int i=0; i<features.length; i++ )
		{
			int  seg_no = (int) java.lang.Math.floor( ( features[ i ] - min_f ) / delta_f );
			if ( seg_no <= 0 )
				seg_no = 1;
			if ( seg_no >= histogram_size - 1 )
				seg_no = histogram_size - 2;
			histogram[ seg_no ] ++;
		}
		
		return  histogram;
	}
	
	// �����ʂ̓x�����z�i�q�X�g�O�����j���v�Z
	//�i�S�̂������̋�Ԃɕ����邩�Ƃ�����Ԑ����w��A��ԕ��͎�������j
	protected void  makeHistogramsBySize( int segment_size )
	{
		// �x���𒲂ׂ�͈͂�ݒ�
		float  delta_f;
		float  min_f, max_f;
		min_f = features0[ 0 ];
		max_f = features0[ 0 ];
		int  i;
		for ( i=1; i<features0.length; i++ )
		{
			if ( features0[ i ] < min_f )
				min_f = features0[ i ];
			if ( features0[ i ] > max_f )
				max_f = features0[ i ];
		}
		for ( i=0; i<features1.length; i++ )
		{
			if ( features1[ i ] < min_f )
				min_f = features1[ i ];
			if ( features1[ i ] > max_f )
				max_f = features1[ i ];
		}
		
		// �͈́E��Ԑ��ɉ����ċ�ԕ���ݒ�
		delta_f = ( max_f - min_f ) / segment_size;
		
		// �͈́E��ԕ����O�ł���Ζ�肪�o��̂ŁA���ɐݒ�
		if ( max_f == min_f )
		{
			delta_f = 0.01f / segment_size; // �K��
			max_f = min_f + delta_f * segment_size;
		}
		
		// �q�X�g�O�����̐ݒ���L�^
		histogram_min_f = min_f;
		histogram_max_f = max_f;
		histogram_delta_f = delta_f;
		
		// ���[�̋�Ԃ̕��z���O�ɂȂ�悤�ɁA���E�ɋ�Ԃ��P�ǉ�����
		histogram_min_f -= delta_f;
		histogram_max_f += delta_f;
		
		// �q�X�g�O�������쐬
		histogram0 = makeHistogram( features0, histogram_min_f, histogram_max_f, delta_f );
		histogram1 = makeHistogram( features1, histogram_min_f, histogram_max_f, delta_f );
	}
	
	// �����ʂ̓x�����z�i�q�X�g�O�����j���v�Z
	//�i�e��Ԃ̋�ԕ����w��A��Ԑ��͎�������j
	protected void  makeHistogramsByWidth( float delta_f )
	{
		// �x���𒲂ׂ�͈͂�ݒ�
		float  min_f, max_f;
		min_f = features0[ 0 ];
		max_f = features0[ 0 ];
		int  i;
		for ( i=1; i<features0.length; i++ )
		{
			if ( features0[ i ] < min_f )
				min_f = features0[ i ];
			if ( features0[ i ] > max_f )
				max_f = features0[ i ];
		}
		for ( i=0; i<features1.length; i++ )
		{
			if ( features1[ i ] < min_f )
				min_f = features1[ i ];
			if ( features1[ i ] > max_f )
				max_f = features1[ i ];
		}
		
		// ��ԕ��ɉ����Ĕ͈͂𒲐��i��Ԃ̗��[�l����ԕ��̐����{�ɂȂ�悤�����j
		min_f = (int)( min_f / delta_f ) * delta_f;
		max_f = (int)( max_f / delta_f + 1 ) * delta_f;
		
		// �q�X�g�O�����̐ݒ���L�^
		histogram_min_f = min_f;
		histogram_max_f = max_f;
		histogram_delta_f = delta_f;
		
		// ���[�̋�Ԃ̕��z���O�ɂȂ�悤�ɁA���E�ɋ�Ԃ��P�ǉ�����
		histogram_min_f -= delta_f;
		histogram_max_f += delta_f;
		
		// �q�X�g�O�������쐬
		histogram0 = makeHistogram( features0, histogram_min_f, histogram_max_f, delta_f );
		histogram1 = makeHistogram( features1, histogram_min_f, histogram_max_f, delta_f );
	}


	//
	//  ������ԕ`��̂��߂̓������\�b�h
	//
	
	// 臒l��`��
	protected void  drawThreshold( GraphViewer gv )
	{
		// 臒l��`��
		GraphPoint  data[];
		data = new GraphPoint[ 1 ];
		data[ 0 ] = new GraphPoint();
		data[ 0 ].x = getThreshold();
		data[ 0 ].y = 0.0f;
//		gv.addFigure( GraphViewer.FIG_Y_LINE, Color.GREEN, data );
		gv.addFigure( GraphViewer.FIG_Y_LINE, Color.MAGENTA, data );
	}
	
	// �f�[�^���z���U�z�}�ŕ`��
	protected void  drawScatteredGraph( GraphViewer gv )
	{
		drawScatteredGraph( gv, 0.0f, 0.0f );
	}
	
	// �f�[�^���z���U�z�}�ŕ`��
	protected void  drawScatteredGraph( GraphViewer gv, float y0, float y1 )
	{
		// �e�f�[�^���U�z�}�ŕ`��
		//�i�����ʂ� x���W�Ƃ��āA�e�T���v����_�ŕ`��j
		//�iy���W�́A�w�肳�ꂽ�͈� y0�`y1 �̊ԂɁA���Ԃɕ��ׂ�j
		//�i���� y���W�ɕ`�悷��ƁA�_���d�Ȃ��Č��ɂ����̂ŁA�͈͂��w�肵�āA���������炵�Ȃ���`��j
		GraphPoint  data[];
		data = new GraphPoint[ features0.length ];
		for ( int i=0; i<features0.length; i++ )
		{
			data[ i ] = new GraphPoint();
			data[ i ].x = features0[ i ];
			data[ i ].y = ( y1 - y0 ) * ( (float)i / features0.length ) + y0;
		}
		gv.addFigure( GraphViewer.FIG_SCATTERED, Color.RED, data );
		
		data = new GraphPoint[ features1.length ];
		for ( int i=0; i<features1.length; i++ )
		{
			data[ i ] = new GraphPoint();
			data[ i ].x = features1[ i ];
			data[ i ].y = ( y1 - y0 ) * ( (float)i / features1.length ) + y0;
		}
		gv.addFigure( GraphViewer.FIG_SCATTERED, Color.BLUE, data );
	}
	
	// ���ϒl���c���ŕ`��
	protected void  drawAverage( GraphViewer gv )
	{
		// ���O���[�v�̓����ʂ̕��ϒl��`��
		GraphPoint  data[];
		data = new GraphPoint[ 1 ];
		data[ 0 ] = new GraphPoint();
		data[ 0 ].x = average0;
		data[ 0 ].y = 0.0f;
		gv.addFigure( GraphViewer.FIG_Y_LINE, new Color( 1.0f, 0.5f, 0.5f ), data );
		
		data = new GraphPoint[ 1 ];
		data[ 0 ] = new GraphPoint();
		data[ 0 ].x = average1;
		data[ 0 ].y = 0.0f;
		gv.addFigure( GraphViewer.FIG_Y_LINE, new Color( 0.5f, 0.5f, 1.0f ), data );
	}

	// �x�����z��_�O���t�ŕ`��
	protected void  drawHistogram( GraphViewer gv )
	{
		// �x�����z�O���t��`��
		GraphPoint  data[];
		data = new GraphPoint[ histogram0.length ];
		int  i;
		for ( i=0; i<histogram0.length; i++ )
		{
			data[ i ] = new GraphPoint();
			data[ i ].x = histogram_min_f + histogram_delta_f * ( i + 0.5f - 0.1f ); // �������炷
			data[ i ].y = histogram0[ i ];
		}
		gv.addFigure( GraphViewer.FIG_BAR, Color.RED, data, default_histogram_delta * 0.8f );

		data = new GraphPoint[ histogram1.length ];
		for ( i=0; i<histogram1.length; i++ )
		{
			data[ i ] = new GraphPoint();
			data[ i ].x = histogram_min_f + histogram_delta_f * ( i + 0.5f + 0.1f ); // �������炷
			data[ i ].y = histogram1[ i ];
		}
		gv.addFigure( GraphViewer.FIG_BAR, Color.BLUE, data, default_histogram_delta * 0.8f );
	}
};
