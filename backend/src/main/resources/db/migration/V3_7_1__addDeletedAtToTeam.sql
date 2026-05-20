ALTER TABLE team
    ADD COLUMN status text default 'ACTIVE',
    ADD COLUMN marked_as_archived_at timestamp;

CREATE OR REPLACE FUNCTION prevent_archived_team_update()
    RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status = 'ARCHIVED' THEN

        IF NEW.status != 'ACTIVE' THEN
            RAISE EXCEPTION 'DB Write-Lock Active: Cannot modify an archived team (ID: %).', OLD.id;
        END IF;

    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER enforce_archived_team_lock
BEFORE UPDATE ON team
FOR EACH ROW
EXECUTE FUNCTION prevent_archived_team_update();
