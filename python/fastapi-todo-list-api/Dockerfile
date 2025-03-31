FROM python:3.11.8-slim-bullseye

# Turn off python output buffering
ENV PYTHONUNBUFFERED=1

WORKDIR /app

ENV PYTHON_VENV="/app/venv"
ENV PATH="$PYTHON_VENV/bin:$PATH"

# Create Python virtual environment
RUN python3 -m venv "$PYTHON_VENV"

# Install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY todo .

ENV PORT="8000"
CMD ["bash",  "-c", "uvicorn --app-dir=src app:app --host=0.0.0.0 --port=${PORT}"]
