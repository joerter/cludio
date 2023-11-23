-- :name classes-by-date-range :? :*
-- :doc Get all classes for a date range
select 
	cs.id as class_schedule_id, 
	cs.datetime, 
	c.id as class_id, 
	c.name, 
	t.id as teacher_id, 
	t.first_name, t.last_name
from class_schedule cs
join class c on c.id = cs.class_id
join class_repetition cr on cr.id = cs.class_repetition_id
join teacher t on t.id = cr.teacher_id
where cs.datetime >= :start-date and cs.datetime <= :end-date
order by datetime
