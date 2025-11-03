# Tiltfile (root of repo)

docker_compose('devops/docker-compose.dev.yml')

# Optional labels and dependencies for UI grouping
dc_resource('postgres', labels=['infrastructure'])
dc_resource('redis', labels=['infrastructure'])
# dc_resource('smtp', labels=['infrastructure'])
dc_resource('mailhog', labels=['infrastructure'])

dc_resource('authentication', labels=['backend'], resource_deps=['postgres'])
dc_resource('profile', labels=['backend'], resource_deps=['postgres', 'authentication'])
dc_resource('provider', labels=['backend'], resource_deps=['postgres', 'authentication'])
dc_resource('reservation', labels=['backend'], resource_deps=['postgres', 'authentication'])
dc_resource('bff', labels=['backend'], resource_deps=['authentication', 'profile', 'provider', 'reservation'])
dc_resource('frontend', labels=['frontend'], resource_deps=['bff'])

# Optional local tasks
local_resource(
    'run-tests',
    'echo "Running tests..." && cd /home/TennisTime/tennistime && mvn test',
    deps=['/home/TennisTime/tennistime/**/src/**/*.java'],
    labels=['testing'],
    auto_init=False
)

local_resource(
    'db-migrate',
    'echo "Running database migrations..." && cd /home/TennisTime/tennistime && ./scripts/migrate.sh',
    deps=['/home/TennisTime/tennistime/**/src/main/resources/db/migration/*.sql'],
    labels=['database'],
    auto_init=False
)
