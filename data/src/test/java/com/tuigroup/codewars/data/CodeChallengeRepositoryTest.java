package com.tuigroup.codewars.data;

import com.google.common.base.Optional;
import com.tuigroup.codewars.data.local.CodeChallengeDao;
import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;
import com.tuigroup.codewars.data.mapper.CodeChallengeMapper;
import com.tuigroup.codewars.data.remote.CodeChallengeRestApi;
import com.tuigroup.codewars.data.remote.model.CodeChallenge;
import com.tuigroup.codewars.data.util.Resource;
import com.tuigroup.codewars.data.util.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CodeChallengeRepositoryTest {

    private static final String CHALLENGE_ID_TEST = "challenge_id_test";

    @Mock
    private CodeChallengeRestApi codeChallengeRestApi;
    @Mock
    private CodeChallengeDao codeChallengeDao;
    @Mock
    private CodeChallengeMapper codeChallengeMapper;

    private CodeChallengeRepository codeChallengeRepository;


    @Before
    public void setUp() {
        codeChallengeRepository = new CodeChallengeRepository(
                codeChallengeRestApi, codeChallengeDao, codeChallengeMapper);
    }

    @Test
    public void getChallengeByIdFromApi() {
        CodeChallenge codeChallenge = TestUtil.createCodeChallenge(CHALLENGE_ID_TEST, "cat");
        CodeChallengeEntity codeChallengeEntity = TestUtil.createCodeChallengeEntity(CHALLENGE_ID_TEST, "cat");
        List<CodeChallengeEntity> codeChallengeEntities = Arrays.asList(codeChallengeEntity);
        Mockito.when(codeChallengeRestApi.getCodeChallenge(CHALLENGE_ID_TEST)).thenReturn(Single.just(codeChallenge));
        Mockito.when(codeChallengeMapper.mapFromApiToEntity(codeChallenge)).thenReturn(codeChallengeEntity);
        Mockito.when(codeChallengeDao.getChallengeById(CHALLENGE_ID_TEST)).thenReturn(Flowable.just(codeChallengeEntities));

        TestSubscriber<Resource<Optional<CodeChallengeEntity>>> testObserver = codeChallengeRepository.getChallenge(CHALLENGE_ID_TEST).test();

        assertTrue(testObserver.values().get(0).status == Status.LOCAL);
        assertTrue(testObserver.values().get(0).data.get().getId().equals(CHALLENGE_ID_TEST));
    }
}
