FROM python:3.9

RUN pip install classla fastapi uvicorn

EXPOSE 5010

COPY ./main.py /main.py

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "5010"]