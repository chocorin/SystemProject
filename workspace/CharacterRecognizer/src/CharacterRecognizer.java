import java.awt.image.BufferedImage;
//import java.awt.Color;


//
//  �����摜�F���N���X
//
class  CharacterRecognizer
{
	// �����ʂ̕]���p�I�u�W�F�N�g
	protected FeatureEvaluater    feature_evaluater;
	
	// 臒l�̌���p�I�u�W�F�N�g
	protected ThresholdDeterminer  threshold_determiner;
	
	// �w�K�Ɏg�p�����摜�̓�����
	protected float  features0[];
	protected float  features1[];
	
	
	// �����ʂ̕]���p�I�u�W�F�N�g��ݒ�
	public void  setFeatureEvaluater( FeatureEvaluater fe )
	{
		feature_evaluater = fe;
	}
	
	// 臒l�̌v�Z�p�I�u�W�F�N�g��ݒ�
	public void  setThresholdDeterminer( ThresholdDeterminer td )
	{
		threshold_determiner = td;
	}
	
	// �����ʂ̕]���p�I�u�W�F�N�g���擾
	public FeatureEvaluater  getFeatureEvaluater()
	{
		return  feature_evaluater;
	}
	
	// 臒l�̌v�Z�p�I�u�W�F�N�g���擾
	public ThresholdDeterminer  getThresholdDeterminer()
	{
		return  threshold_determiner;
	}
	
	
	// �^����ꂽ�Q�̃O���[�v�̉摜�f�[�^�𔻕ʂ���悤�ȓ����ʂ�臒l���v�Z
	public void  train( BufferedImage[] images0, BufferedImage[] images1 )
	{
		// �v�Z�p�I�u�W�F�N�g�����ݒ�ł���Ώ����͍s��Ȃ�
		if ( ( feature_evaluater == null ) || ( threshold_determiner == null ) )
			return;
		
		// �e�摜�̓����ʂ��v�Z
		features0 = new float[ images0.length ];
		features1 = new float[ images1.length ];
		for ( int i=0; i<images0.length; i++ )
		{
			if ( images0[ i ] != null )
				features0[ i ] = feature_evaluater.evaluate( images0[ i ] );
		}
		for ( int i=0; i<images1.length; i++ )
		{
			if ( images1[ i ] != null )
				features1[ i ] = feature_evaluater.evaluate( images1[ i ] );
		}

		// �����ʂ̕��z����Q�̃O���[�v�����ʂ���悤��臒l������
		threshold_determiner.determine( features0, features1 );
	}
	
	// �w�K���ʂɊ�Â��ė^����ꂽ�摜�𔻕ʁi���ʂ����摜�̎�� 0 or 1 ��Ԃ��j
	public int  recognizeCharacter( BufferedImage image )
	{
		// �v�Z�p�I�u�W�F�N�g�����ݒ�ł���Ώ����͍s��Ȃ�
		if ( ( feature_evaluater == null ) || ( threshold_determiner == null ) )
			return  -1;
		if ( image == null )
			return  -1;
		
		// �^����ꂽ�摜�̓����ʂ��v�Z
		float  feature = feature_evaluater.evaluate( image );
		
		// �^����ꂽ�摜��F��
		return  threshold_determiner.recognize( feature );
	}

	
	//
	//  �O���t�`��
	//
	
	// ������Ԃ̃f�[�^��`��i�O���t�I�u�W�F�N�g�Ƀf�[�^��ݒ�j
	public void  drawGraph( GraphViewer gv )
	{
		// �O���t���N���A
		gv.clearFigure();

		// ������Ԃ̃f�[�^�E臒l�̃f�[�^���O���t�ɐݒ�
		threshold_determiner.drawGraph( gv );
		
		// �O���t�̕`��͈͂������ݒ�
		gv.setGraphAreaAuto();
	}
}