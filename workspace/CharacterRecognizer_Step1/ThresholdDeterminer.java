//
//  �P�����̓����ʂ�臒l�v�Z�N���X�̃C���^�[�t�F�[�X
//
interface  ThresholdDeterminer
{
	// 臒l�̌�����@�̖��O��Ԃ�
	public String  getThresholdName();
	
	// ���O���[�v�̓����ʂ���臒l������
	public void  determine( float[] features0, float[] features1 );
	
	// �^����ꂽ�����ʂ���ǂ���̕������𔻒�
	public int  recognize( float feature );
	
	// 臒l��Ԃ�
	public float  getThreshold();
	
	// ������Ԃ̃f�[�^���O���t�ɕ`��i�O���t�I�u�W�F�N�g�ɐ}�`�f�[�^��ݒ�j
	public void  drawGraph( GraphViewer gv );
};
