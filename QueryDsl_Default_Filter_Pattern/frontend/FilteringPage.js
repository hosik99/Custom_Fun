/* 
  회원 필터링 객체 초기화 및 서버에 전달하는 API 연결
*/
export default function MembersBox() {
  const showSnack = useSnack();

  const [filters, setFilters] = useState({
    email: "",
    nickname: "",
    countryCode: null,
    role: null,
    status: null,
  });

  const [dateFilters, setDateFilters] = useState({
    dateOption: null,
    dateStart: "",
    dateEnd: "",
  })

  const [pageOption, setPageOption] = useState({
    totalElements: 0,
    totalPages: 0,
    page: 0,
    size: 20,
  })

  // useState 속성 헨들러
  const handleFilterChange = (key) => (e) => { setFilters((prev) => ({ ...prev, [key]: e.target.value }));};
  const updateFilters = useCallback( (field, value) => { setFilters((prev) => ({ ...prev, [field]: value })); }, [] );

  // 필터링 & 페이징 이용, 여러 유저 검색
  const getFilterMemberAPI = useCallback(
    async ( {filter,pageable} ) => {
      const {success, message, data } = await filterMemberAPI( {filter,pageable} );
      if(success){
        setData(data.content);
        setPageOption(prev => ({ ...prev,
          totalElements: data.totalElements,
          totalPages: data.totalPages,
          page: data.number,
          size: data.size,
        }));
      }else{
        showSnack("error", message);
      }
  },[]);

  const handleSearchBtn = useCallback(
    ({ page = pageOption.page, size = pageOption.size } = {}) => {
      const filter = {
        email:       filters.email,
        nickname:    filters.nickname,
        countryCode: filters.countryCode,
        role:        filters.role,
        status:      filters.status,
        dateOption:  dateFilters.dateOption,
        dateStart:   dateFilters.dateStart,
        dateEnd:     dateFilters.dateEnd,
      };
      getFilterMemberAPI({ filter, pageable: { page, size } });
    },
    [filters, dateFilters, getFilterMemberAPI, pageOption.page, pageOption.size]
  );

  return (
    <Box>
      {/* 필터 바 */}
      <Box display="flex" alignItems="center" flexWrap="wrap" gap={1} mb={2}>

        <CountrySelector
          value={filters.countryCode}
          onChange={newValue => updateFilters("countryCode", newValue)}
        />

        <TextField
          size="small"
          label="Email"
          value={filters.email}
          onChange={handleFilterChange("email")}
        />

        <TextField
          size="small"
          label="Nickname"
          value={filters.nickname}
          onChange={handleFilterChange("nickname")}
        />

        <MemberRoleSelector
          value={filters.role}
          onChange={newValue => updateFilters("role", newValue)}
        />

        <MemberStatusSelector
          value={filters.status}
          onChange={newValue => updateFilters("status", newValue)}
        />

        <DateSelector
            addValue={['createdAt','updatedAt']}
            dateOption={dateFilters.dateOption}
            dateStart={dateFilters.dateStart}
            dateEnd={dateFilters.dateEnd}
            onChange={newValues => setDateFilters(prev => ({ ...prev, ...newValues }))}
        />
      </Box>

    </Box>
  );
}
