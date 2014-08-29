insert into city_website (city)
select id from (
select 
    dt.id,
    dt.name,
    dt.state,
    @counter:=case
        when @state = dt.state then @counter + 1
        else 1
    end as rank,
    @state:=dt.state as st
from
    ((select @counter:=- 1) cnt, (select @state:='') st, (select 
        *
    from
        city
    where
        feature = 'Civil' and state not in ('AK','HI','VI','PR')
    order by state asc , population desc) dt)
) ranked where rank <= 60;