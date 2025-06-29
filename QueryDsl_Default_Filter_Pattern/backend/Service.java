
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRps;

    // Pageable과 전달 받은 필터링 값을 이용, 필터링된 멤버 목록 반환
    public Page<MemberResponse> filterMembers(MemberFilterRequest filterDTO, Pageable pageable) {
        Predicate predicate = FilterPredicateManager.buildMembersFilterPredicate(filterDTO);
        return memberRps.findAll(predicate, pageable)
            .map(MemberResponse::toDto);
    }

}
