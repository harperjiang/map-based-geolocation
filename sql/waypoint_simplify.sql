create temporary table road_factor as (select road,count(id)/10 as factor from road_waypoint group by road having count(id) > 10);

create temporary table road_newseq as
select max(t.id) id, t.seq
from (
select wp.id, wp.road, wp.point_x, wp.point_y, floor(wp.sequence / fac.factor) as seq
from road_waypoint wp
join road_factor fac ON fac.road = wp.road) t group by t.road, t.seq;

insert into road_waypoint_simple (id,road,point_x,point_y,sequence)
(
select wp.id,wp.road,wp.point_x,wp.point_y,nseq.seq 
from road_waypoint wp 
join road_newseq nseq on wp.id = nseq.id
);

create temporary table road_small as
select road from road_waypoint group by road having count(id) <= 10;

insert into road_waypoint_simple(id,road,point_x,point_y,sequence)
(select wp.id,wp.road,wp.point_x,wp.point_y,wp.sequence from road_waypoint wp join road_small small on wp.road = small.road);