-- spatial index for place location
create index if not exists place_location_idx on place using gist (location);