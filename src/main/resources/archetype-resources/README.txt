You can use run.sh to start the application in embedded mode. This will actually rebuild the executable JAR (including testing).
It takes a spring profile argument.

You might need to chmod it to run locally, e.g.:

chmod u+x run.sh

Running:

./run.sh A B

where A & B are optional
A = spring profile, if not provided defaults to development
B = skip or don't provide. If 'skip' then no tests will run, e.g. ./run.sh development skip < runs in development mode without running the tests (will package though)

