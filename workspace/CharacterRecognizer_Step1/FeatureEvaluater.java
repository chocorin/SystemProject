import java.awt.image.BufferedImage;
import java.awt.Graphics;


//
//  �����摜�̓����ʌv�Z�N���X�̃C���^�[�t�F�[�X
//
interface  FeatureEvaluater
{
	// �����ʂ̖��O��Ԃ�
	public String  getFeatureName();
	
	// �����摜����P�����̓����ʂ��v�Z����
	public float  evaluate( BufferedImage image );
	
	// �Ō�ɍs���������ʌv�Z�̌��ʂ�`�悷��
	public void  paintImageFeature( Graphics g );
};
